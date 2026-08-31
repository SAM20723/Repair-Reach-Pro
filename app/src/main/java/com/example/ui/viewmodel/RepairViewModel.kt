package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DayAvailability
import com.example.data.model.JobItem
import com.example.data.model.JobStatus
import com.example.data.model.ShiftSlot
import com.example.data.model.TechnicianProfile
import com.example.data.repository.RepairReachRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class RepairTab(val title: String) {
  TODAY("Today"),
  EXECUTION("Job Details"),
  SCHEDULE("Schedule"),
  AVAILABILITY("Availability"),
  PROFILE("Profile")
}

enum class JobFilter(val label: String) {
  ALL("All"),
  PENDING("Pending"),
  EN_ROUTE("En Route"),
  IN_PROGRESS("In Progress"),
  COMPLETED("Completed")
}

data class MetricSummary(
  val totalJobsToday: Int,
  val completedJobsToday: Int,
  val inProgressJobsToday: Int,
  val pendingJobsToday: Int,
  val capacityHoursBooked: Double,
  val capacityHoursTotal: Double,
  val onTimePercentage: Int
)

class RepairViewModel(
  private val repository: RepairReachRepository = RepairReachRepository()
) : ViewModel() {

  private val _currentTab = MutableStateFlow(RepairTab.TODAY)
  val currentTab: StateFlow<RepairTab> = _currentTab.asStateFlow()

  private val _todayFilter = MutableStateFlow(JobFilter.ALL)
  val todayFilter: StateFlow<JobFilter> = _todayFilter.asStateFlow()

  private val _toastMessage = MutableStateFlow<String?>(null)
  val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

  // Contact Dialog states
  private val _showCallDialog = MutableStateFlow<JobItem?>(null)
  val showCallDialog: StateFlow<JobItem?> = _showCallDialog.asStateFlow()

  private val _showMessageDialog = MutableStateFlow<JobItem?>(null)
  val showMessageDialog: StateFlow<JobItem?> = _showMessageDialog.asStateFlow()

  // Job Completion dialog state
  private val _showCompleteSummaryDialog = MutableStateFlow<JobItem?>(null)
  val showCompleteSummaryDialog: StateFlow<JobItem?> = _showCompleteSummaryDialog.asStateFlow()

  val jobs: StateFlow<List<JobItem>> = repository.jobs

  val selectedJobId: StateFlow<String> = repository.selectedJobId
  val selectedScheduleDateKey: StateFlow<String> = repository.selectedScheduleDateKey
  val selectedAvailabilityDateKey: StateFlow<String> = repository.selectedAvailabilityDateKey
  val availabilityMap: StateFlow<Map<String, DayAvailability>> = repository.availabilityMap
  val profile: StateFlow<TechnicianProfile> = repository.profile

  // Today's date key
  val todayDateKey = "2026-08-31"

  val todayJobs: StateFlow<List<JobItem>> = repository.jobs.combine(_todayFilter) { allJobs, filter ->
    val todayList = allJobs.filter { it.scheduledDateKey == todayDateKey }
    when (filter) {
      JobFilter.ALL -> todayList
      JobFilter.PENDING -> todayList.filter { it.status == JobStatus.PENDING }
      JobFilter.EN_ROUTE -> todayList.filter { it.status == JobStatus.EN_ROUTE }
      JobFilter.IN_PROGRESS -> todayList.filter { it.status == JobStatus.IN_PROGRESS }
      JobFilter.COMPLETED -> todayList.filter { it.status == JobStatus.COMPLETED }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val metrics: StateFlow<MetricSummary> = repository.jobs.combine(profile) { allJobs, techProfile ->
    val todayList = allJobs.filter { it.scheduledDateKey == todayDateKey }
    val completed = todayList.count { it.status == JobStatus.COMPLETED }
    val inProgress = todayList.count { it.status == JobStatus.IN_PROGRESS || it.status == JobStatus.EN_ROUTE }
    val pending = todayList.count { it.status == JobStatus.PENDING }
    val bookedHours = todayList.sumOf { it.estimatedHours }

    MetricSummary(
      totalJobsToday = todayList.size,
      completedJobsToday = completed,
      inProgressJobsToday = inProgress,
      pendingJobsToday = pending,
      capacityHoursBooked = bookedHours,
      capacityHoursTotal = 8.0,
      onTimePercentage = techProfile.onTimeRatePercent
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    MetricSummary(4, 1, 1, 2, 6.75, 8.0, 98)
  )

  val activeExecutingJob: StateFlow<JobItem?> = repository.jobs.combine(repository.selectedJobId) { allJobs, selectedId ->
    allJobs.find { it.id == selectedId }
      ?: allJobs.find { it.status == JobStatus.IN_PROGRESS }
      ?: allJobs.find { it.status == JobStatus.EN_ROUTE }
      ?: allJobs.firstOrNull { it.scheduledDateKey == todayDateKey }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val scheduleDateJobs: StateFlow<List<JobItem>> = repository.jobs.combine(repository.selectedScheduleDateKey) { allJobs, dateKey ->
    allJobs.filter { it.scheduledDateKey == dateKey }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val selectedDayAvailability: StateFlow<DayAvailability?> = repository.availabilityMap.combine(repository.selectedAvailabilityDateKey) { map, dateKey ->
    map[dateKey]
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  fun selectTab(tab: RepairTab) {
    _currentTab.value = tab
  }

  fun setTodayFilter(filter: JobFilter) {
    _todayFilter.value = filter
  }

  fun openJobDetails(jobId: String) {
    repository.selectJob(jobId)
    _currentTab.value = RepairTab.EXECUTION
  }

  fun advanceSelectedJobLifecycle(jobId: String) {
    val newStatus = repository.advanceJobLifecycle(jobId)
    when (newStatus) {
      JobStatus.EN_ROUTE -> showToast("En route to customer location. GPS navigation active.")
      JobStatus.IN_PROGRESS -> showToast("Marked arrived on site. Timer and work order active.")
      JobStatus.COMPLETED -> {
        showToast("Job marked completed! Service report generated.")
        val job = repository.jobs.value.find { it.id == jobId }
        if (job != null) {
          _showCompleteSummaryDialog.value = job
        }
      }
      null -> {}
      JobStatus.PENDING -> {}
    }
  }

  fun toggleChecklist(jobId: String, itemId: String) {
    repository.toggleChecklistItem(jobId, itemId)
  }

  fun togglePhoto(jobId: String, isAfter: Boolean) {
    repository.togglePhotoCaptured(jobId, isAfter)
    showToast(if (isAfter) "After-repair photo captured ✓" else "Before-repair photo captured ✓")
  }

  fun updateNotes(jobId: String, note: String) {
    repository.updateJobNotes(jobId, note)
  }

  fun setScheduleDate(dateKey: String) {
    repository.setSelectedScheduleDate(dateKey)
  }

  fun setAvailabilityDate(dateKey: String) {
    repository.setSelectedAvailabilityDate(dateKey)
  }

  fun toggleDayBlocked(dateKey: String, isBlocked: Boolean, reason: String) {
    repository.toggleDayBlocked(dateKey, isBlocked, reason)
    showToast(if (isBlocked) "Day blocked: $reason" else "Day unblocked. Shifts active.")
  }

  fun toggleShift(dateKey: String, slot: ShiftSlot, available: Boolean) {
    repository.toggleShiftAvailability(dateKey, slot, available)
  }

  fun openCallDialog(job: JobItem) {
    _showCallDialog.value = job
  }

  fun closeCallDialog() {
    _showCallDialog.value = null
  }

  fun openMessageDialog(job: JobItem) {
    _showMessageDialog.value = job
  }

  fun closeMessageDialog() {
    _showMessageDialog.value = null
  }

  fun closeCompleteSummaryDialog() {
    _showCompleteSummaryDialog.value = null
  }

  fun sendPresetMessage(job: JobItem, messageText: String) {
    _showMessageDialog.value = null
    showToast("Sent SMS to ${job.customer.name}: \"$messageText\"")
  }

  fun triggerCall(job: JobItem) {
    _showCallDialog.value = null
    showToast("Dialing customer ${job.customer.name} at ${job.customer.phone}...")
  }

  fun triggerGpsNavigation(job: JobItem) {
    showToast("Opening turn-by-turn navigation to ${job.customer.address}...")
  }

  fun showToast(message: String) {
    _toastMessage.value = message
  }

  fun clearToast() {
    _toastMessage.value = null
  }
}
