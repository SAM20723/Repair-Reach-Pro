package com.example.data.repository

import com.example.data.model.ChecklistItem
import com.example.data.model.Customer
import com.example.data.model.DayAvailability
import com.example.data.model.Equipment
import com.example.data.model.JobItem
import com.example.data.model.JobPriority
import com.example.data.model.JobStatus
import com.example.data.model.PartUsed
import com.example.data.model.ShiftSlot
import com.example.data.model.TechnicianProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RepairReachRepository {

  private val _jobs = MutableStateFlow<List<JobItem>>(generateInitialJobs())
  val jobs: StateFlow<List<JobItem>> = _jobs.asStateFlow()

  private val _selectedJobId = MutableStateFlow<String>("job-2") // Default to active/in-progress job
  val selectedJobId: StateFlow<String> = _selectedJobId.asStateFlow()

  private val _selectedScheduleDateKey = MutableStateFlow<String>("2026-08-31")
  val selectedScheduleDateKey: StateFlow<String> = _selectedScheduleDateKey.asStateFlow()

  private val _availabilityMap = MutableStateFlow<Map<String, DayAvailability>>(generateInitialAvailability())
  val availabilityMap: StateFlow<Map<String, DayAvailability>> = _availabilityMap.asStateFlow()

  private val _selectedAvailabilityDateKey = MutableStateFlow<String>("2026-08-31")
  val selectedAvailabilityDateKey: StateFlow<String> = _selectedAvailabilityDateKey.asStateFlow()

  private val _profile = MutableStateFlow(TechnicianProfile())
  val profile: StateFlow<TechnicianProfile> = _profile.asStateFlow()

  // State machine transition for a job: Pending -> En Route -> In Progress -> Completed
  fun advanceJobLifecycle(jobId: String): JobStatus? {
    var newStatus: JobStatus? = null
    _jobs.value = _jobs.value.map { job ->
      if (job.id == jobId) {
        val next = when (job.status) {
          JobStatus.PENDING -> JobStatus.EN_ROUTE
          JobStatus.EN_ROUTE -> JobStatus.IN_PROGRESS
          JobStatus.IN_PROGRESS -> JobStatus.COMPLETED
          JobStatus.COMPLETED -> JobStatus.COMPLETED
        }
        newStatus = next
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        job.copy(
          status = next,
          completionTimestamp = if (next == JobStatus.COMPLETED) timeNow else job.completionTimestamp
        )
      } else {
        job
      }
    }
    return newStatus
  }

  fun setJobStatus(jobId: String, status: JobStatus) {
    _jobs.value = _jobs.value.map {
      if (it.id == jobId) it.copy(status = status) else it
    }
  }

  fun selectJob(jobId: String) {
    _selectedJobId.value = jobId
  }

  fun setSelectedScheduleDate(dateKey: String) {
    _selectedScheduleDateKey.value = dateKey
  }

  fun setSelectedAvailabilityDate(dateKey: String) {
    _selectedAvailabilityDateKey.value = dateKey
  }

  fun toggleChecklistItem(jobId: String, itemId: String) {
    _jobs.value = _jobs.value.map { job ->
      if (job.id == jobId) {
        val updatedChecklist = job.checklist.map { item ->
          if (item.id == itemId) item.copy(isCompleted = !item.isCompleted) else item
        }
        job.copy(checklist = updatedChecklist)
      } else {
        job
      }
    }
  }

  fun togglePhotoCaptured(jobId: String, isAfterPhoto: Boolean) {
    _jobs.value = _jobs.value.map { job ->
      if (job.id == jobId) {
        if (isAfterPhoto) {
          job.copy(afterPhotoCaptured = !job.afterPhotoCaptured)
        } else {
          job.copy(beforePhotoCaptured = !job.beforePhotoCaptured)
        }
      } else {
        job
      }
    }
  }

  fun updateJobNotes(jobId: String, notes: String) {
    _jobs.value = _jobs.value.map { job ->
      if (job.id == jobId) job.copy(techNotes = notes) else job
    }
  }

  fun toggleDayBlocked(dateKey: String, isBlocked: Boolean, reason: String = "Time Off / PTO") {
    val currentMap = _availabilityMap.value.toMutableMap()
    val existing = currentMap[dateKey]
    if (existing != null) {
      currentMap[dateKey] = existing.copy(
        isBlocked = isBlocked,
        blockReason = reason,
        morningAvailable = if (isBlocked) false else true,
        afternoonAvailable = if (isBlocked) false else true,
        eveningAvailable = if (isBlocked) false else false
      )
      _availabilityMap.value = currentMap
    }
  }

  fun toggleShiftAvailability(dateKey: String, slot: ShiftSlot, available: Boolean) {
    val currentMap = _availabilityMap.value.toMutableMap()
    val existing = currentMap[dateKey]
    if (existing != null) {
      val updated = when (slot) {
        ShiftSlot.MORNING -> existing.copy(morningAvailable = available)
        ShiftSlot.AFTERNOON -> existing.copy(afternoonAvailable = available)
        ShiftSlot.EVENING -> existing.copy(eveningAvailable = available)
      }
      currentMap[dateKey] = updated
      _availabilityMap.value = currentMap
    }
  }

  fun updateAvailabilityNotes(dateKey: String, notes: String) {
    val currentMap = _availabilityMap.value.toMutableMap()
    val existing = currentMap[dateKey]
    if (existing != null) {
      currentMap[dateKey] = existing.copy(notes = notes)
      _availabilityMap.value = currentMap
    }
  }

  private fun generateInitialJobs(): List<JobItem> {
    return listOf(
      // --- TODAY'S JOBS (Aug 31) ---
      JobItem(
        id = "job-1",
        workOrderNumber = "WO-94821",
        scheduledDateKey = "2026-08-31",
        scheduledDateDisplay = "Today, Aug 31",
        scheduledTimeStart = "08:30 AM",
        scheduledTimeEnd = "10:15 AM",
        estimatedHours = 1.75,
        customer = Customer(
          name = "Eleanor Vance",
          phone = "(555) 234-8901",
          address = "1424 Westridge Blvd",
          unitOrSuite = "Suite 204",
          propertyType = "Commercial Office",
          gateCode = "#4920",
          accessNotes = "Check in with front desk security. Service elevator on north corridor."
        ),
        equipment = Equipment(
          name = "Carrier WeatherExpert 5-Ton Rooftop Unit",
          brand = "Carrier",
          modelNumber = "48TCED06A2A5",
          serialNumber = "CR-2022-849102",
          installYear = "2022",
          locationInBuilding = "Roof Area Zone B (North Wing)",
          warrantyStatus = "Active Parts & Compressor"
        ),
        issueTitle = "2nd Floor AC Unit Blowing Warm Air",
        issueDescription = "Thermostat set to 70°F but ambient temperature reached 79°F in conference wing. Suspected contactor or capacitor fault.",
        status = JobStatus.COMPLETED,
        priority = JobPriority.STANDARD,
        distanceMiles = 3.2,
        travelTimeMinutes = 10,
        checklist = listOf(
          ChecklistItem("c1-1", "Inspect dual run capacitor microfarad rating", isCompleted = true),
          ChecklistItem("c1-2", "Check contactor points and 24V control voltage", isCompleted = true),
          ChecklistItem("c1-3", "Test condenser fan motor amp draw", isCompleted = true),
          ChecklistItem("c1-4", "Verify refrigerant subcooling & superheat", isCompleted = true),
          ChecklistItem("c1-5", "Replace faulty 45/5 MFD capacitor & test cooling cycle", isCompleted = true)
        ),
        partsUsed = listOf(
          PartUsed("p1-1", "Titan HD Dual Run Capacitor 45/5 MFD", "CAP-45-5-R", 1, 42.50),
          PartUsed("p1-2", "Siemens 2-Pole 30A Contactor", "CON-2P-30A", 1, 34.00)
        ),
        techNotes = "Replaced degraded 45/5 MFD capacitor. Unit started immediately and reached 18°F delta T across coil. System verified operating within spec.",
        invoiceAmount = 245.00,
        completionTimestamp = "10:12 AM",
        beforePhotoCaptured = true,
        afterPhotoCaptured = true
      ),

      JobItem(
        id = "job-2",
        workOrderNumber = "WO-94822",
        scheduledDateKey = "2026-08-31",
        scheduledDateDisplay = "Today, Aug 31",
        scheduledTimeStart = "11:00 AM",
        scheduledTimeEnd = "01:00 PM",
        estimatedHours = 2.0,
        customer = Customer(
          name = "David Miller",
          phone = "(555) 872-3144",
          address = "8840 Summit Ridge Dr",
          unitOrSuite = "",
          propertyType = "Residential",
          gateCode = "#8840",
          accessNotes = "Driveway on left side. Outdoor unit is behind cedar fence gate on east side. Dog will be kept inside."
        ),
        equipment = Equipment(
          name = "Trane XV20i Variable Speed Heat Pump",
          brand = "Trane",
          modelNumber = "4TWV0036A1000C",
          serialNumber = "TR-2023-772910",
          installYear = "2023",
          locationInBuilding = "East Yard Ground Pad",
          warrantyStatus = "10-Year Factory Warranty"
        ),
        issueTitle = "Heat Pump Inverter Board Tripping / Error 79",
        issueDescription = "System shuts down intermittently after 10 minutes of run time. Diagnostic LED code on outdoor unit indicates DC bus voltage fault.",
        status = JobStatus.IN_PROGRESS,
        priority = JobPriority.HIGH,
        distanceMiles = 4.6,
        travelTimeMinutes = 14,
        checklist = listOf(
          ChecklistItem("c2-1", "Inspect DC link reactor and drive module wiring harness", isCompleted = true),
          ChecklistItem("c2-2", "Measure incoming line voltage under full compressor load", isCompleted = true),
          ChecklistItem("c2-3", "Check compressor windings resistance to ground (Megger test)", isCompleted = false),
          ChecklistItem("c2-4", "Verify electronic expansion valve (EEV) step position", isCompleted = false),
          ChecklistItem("c2-5", "Flash latest inverter control board firmware update", isCompleted = false)
        ),
        partsUsed = listOf(
          PartUsed("p2-1", "Trane OEM Inverter Drive Assembly", "MOD02914", 1, 380.00)
        ),
        techNotes = "DC bus voltage fluctuations confirmed at 310V DC under ramp up. Checking harness grounding and reactor connection.",
        invoiceAmount = 420.00,
        beforePhotoCaptured = true,
        afterPhotoCaptured = false
      ),

      JobItem(
        id = "job-3",
        workOrderNumber = "WO-94823",
        scheduledDateKey = "2026-08-31",
        scheduledDateDisplay = "Today, Aug 31",
        scheduledTimeStart = "02:00 PM",
        scheduledTimeEnd = "03:30 PM",
        estimatedHours = 1.5,
        customer = Customer(
          name = "Sophia Martinez",
          phone = "(555) 491-0082",
          address = "512 Oakwood Terrace",
          unitOrSuite = "",
          propertyType = "Residential",
          gateCode = "Call on box #14",
          accessNotes = "Utility room accessible through garage side door. Lockbox code is 5521."
        ),
        equipment = Equipment(
          name = "Rheem Performance Plus Tankless Water Heater",
          brand = "Rheem",
          modelNumber = "ECO200DVLN3-1",
          serialNumber = "RH-2021-992140",
          installYear = "2021",
          locationInBuilding = "Attached Garage Utility Wall",
          warrantyStatus = "Active (5-Year Heat Exchanger)"
        ),
        issueTitle = "Tankless Heater Error Code 11 / Ignition Failure",
        issueDescription = "Customer has no hot water. Unit attempts ignition 3 times then throws Error 11 on digital remote display.",
        status = JobStatus.PENDING,
        priority = JobPriority.STANDARD,
        distanceMiles = 7.1,
        travelTimeMinutes = 18,
        checklist = listOf(
          ChecklistItem("c3-1", "Check gas inlet supply static and dynamic pressure", isCompleted = false),
          ChecklistItem("c3-2", "Inspect flame rod sensor and igniter electrode gap", isCompleted = false),
          ChecklistItem("c3-3", "Clean condensate trap and intake air screen", isCompleted = false),
          ChecklistItem("c3-4", "Test exhaust vent for blockage or backdraft damper stick", isCompleted = false),
          ChecklistItem("c3-5", "Run descaling flush through isolation valves", isCompleted = false)
        ),
        partsUsed = emptyList(),
        techNotes = "Customer noted ignition clicking noise started yesterday afternoon.",
        invoiceAmount = 195.00
      ),

      JobItem(
        id = "job-4",
        workOrderNumber = "WO-94824",
        scheduledDateKey = "2026-08-31",
        scheduledDateDisplay = "Today, Aug 31",
        scheduledTimeStart = "04:30 PM",
        scheduledTimeEnd = "06:00 PM",
        estimatedHours = 1.5,
        customer = Customer(
          name = "Nexus Biotech Lab",
          phone = "(555) 732-9011",
          address = "2100 Innovation Way",
          unitOrSuite = "Suite 400",
          propertyType = "Commercial Bio Facility",
          gateCode = "Security Badge Required",
          accessNotes = "High priority cleanroom area. Must sign visitor NDA at loading dock gate 4."
        ),
        equipment = Equipment(
          name = "Master-Bilt Walk-In Environmental Cold Room",
          brand = "Master-Bilt",
          modelNumber = "MBS-80-R448A",
          serialNumber = "MB-2020-331009",
          installYear = "2020",
          locationInBuilding = "Cleanroom Basement Level B2",
          warrantyStatus = "Commercial SLA Platinum"
        ),
        issueTitle = "Walk-in Temp Spike to +8°C (Threshold +4°C)",
        issueDescription = "Biological sample holding room temperature alarm triggered at 14:15. Evaporator coil frosting over on left fan bank.",
        status = JobStatus.PENDING,
        priority = JobPriority.EMERGENCY,
        distanceMiles = 11.4,
        travelTimeMinutes = 24,
        checklist = listOf(
          ChecklistItem("c4-1", "Check electric defrost heater elements and defrost termination switch", isCompleted = false),
          ChecklistItem("c4-2", "Inspect evaporator fan delay thermostat", isCompleted = false),
          ChecklistItem("c4-3", "Verify suction line solenoid valve operation", isCompleted = false),
          ChecklistItem("c4-4", "Manually trigger hot gas / electric defrost cycle", isCompleted = false)
        ),
        partsUsed = emptyList(),
        techNotes = "Critical pharmaceutical inventory inside. Dispatch requested highest priority ETA.",
        invoiceAmount = 550.00
      ),

      // --- TOMORROW (Sep 1) ---
      JobItem(
        id = "job-5",
        workOrderNumber = "WO-94830",
        scheduledDateKey = "2026-09-01",
        scheduledDateDisplay = "Tue, Sep 1",
        scheduledTimeStart = "09:00 AM",
        scheduledTimeEnd = "11:30 AM",
        estimatedHours = 2.5,
        customer = Customer(
          name = "GreenLeaf Dental Clinic",
          phone = "(555) 674-1290",
          address = "330 Medical Parkway",
          unitOrSuite = "Suite 110",
          propertyType = "Commercial Medical",
          gateCode = "",
          accessNotes = "Rear parking lot entrance."
        ),
        equipment = Equipment(
          name = "Lennox Commercial Multi-Split VRF",
          brand = "Lennox",
          modelNumber = "LNX-VRF-36K",
          serialNumber = "LX-2023-1194",
          installYear = "2023"
        ),
        issueTitle = "Quarterly Preventative Maintenance & Filter Swap",
        issueDescription = "Full seasonal service: coil sanitization, belt tension inspection, electrical torque checks.",
        status = JobStatus.PENDING,
        priority = JobPriority.STANDARD,
        distanceMiles = 5.2,
        travelTimeMinutes = 15
      ),
      JobItem(
        id = "job-6",
        workOrderNumber = "WO-94831",
        scheduledDateKey = "2026-09-01",
        scheduledDateDisplay = "Tue, Sep 1",
        scheduledTimeStart = "01:00 PM",
        scheduledTimeEnd = "03:00 PM",
        estimatedHours = 2.0,
        customer = Customer(
          name = "Robert Chen",
          phone = "(555) 339-8120",
          address = "741 Highland Ave",
          unitOrSuite = "",
          propertyType = "Residential",
          gateCode = "Key in mailbox",
          accessNotes = "Basement access from outside stairwell."
        ),
        equipment = Equipment(
          name = "Goodman 96% Two-Stage Gas Furnace",
          brand = "Goodman",
          modelNumber = "GMVM970803BNA",
          serialNumber = "GD-2019-4819"
        ),
        issueTitle = "Pre-Winter Heating Inspection & Blower Motor Tune",
        issueDescription = "Annual certified combustion efficiency analysis and static pressure verification.",
        status = JobStatus.PENDING,
        priority = JobPriority.STANDARD,
        distanceMiles = 8.7,
        travelTimeMinutes = 20
      ),
      JobItem(
        id = "job-7",
        workOrderNumber = "WO-94832",
        scheduledDateKey = "2026-09-01",
        scheduledDateDisplay = "Tue, Sep 1",
        scheduledTimeStart = "03:45 PM",
        scheduledTimeEnd = "05:15 PM",
        estimatedHours = 1.5,
        customer = Customer(
          name = "Austin Coffee Roasters",
          phone = "(555) 890-4411",
          address = "1200 E 6th Street",
          propertyType = "Commercial Hospitality"
        ),
        equipment = Equipment(
          name = "True GDM-49 Double Glass Merchandiser Cooler",
          brand = "True",
          modelNumber = "GDM-49-HC-TSL01",
          serialNumber = "TR-2022-7718"
        ),
        issueTitle = "Milk Cooler Condensing Unit Gasket Leak",
        issueDescription = "Door perimeter sweating and warm beverage shelf temp.",
        status = JobStatus.PENDING,
        priority = JobPriority.HIGH,
        distanceMiles = 3.8,
        travelTimeMinutes = 12
      ),

      // --- WEDNESDAY (Sep 2) ---
      JobItem(
        id = "job-8",
        workOrderNumber = "WO-94840",
        scheduledDateKey = "2026-09-02",
        scheduledDateDisplay = "Wed, Sep 2",
        scheduledTimeStart = "08:30 AM",
        scheduledTimeEnd = "11:00 AM",
        estimatedHours = 2.5,
        customer = Customer(
          name = "The Grand Hotel",
          phone = "(555) 441-2900",
          address = "100 Boardwalk Way",
          propertyType = "Commercial Hotel"
        ),
        equipment = Equipment(
          name = "York Chiller Central Plant Pump #3",
          brand = "York",
          modelNumber = "YMC2-S1310EB",
          serialNumber = "YK-2021-998"
        ),
        issueTitle = "Chilled Water Primary Pump Seal Replacement",
        issueDescription = "Mechanical seal dripping 3 drops/min. Replace mechanical carbon ceramic seal.",
        status = JobStatus.PENDING,
        priority = JobPriority.STANDARD,
        distanceMiles = 9.5,
        travelTimeMinutes = 22
      ),
      JobItem(
        id = "job-9",
        workOrderNumber = "WO-94841",
        scheduledDateKey = "2026-09-02",
        scheduledDateDisplay = "Wed, Sep 2",
        scheduledTimeStart = "01:30 PM",
        scheduledTimeEnd = "04:30 PM",
        estimatedHours = 3.0,
        customer = Customer(
          name = "Elena Rostova",
          phone = "(555) 918-6721",
          address = "420 Laurel Canyon Rd",
          propertyType = "Residential Luxury"
        ),
        equipment = Equipment(
          name = "Mitsubishi Diamond Multi-Zone Ductless",
          brand = "Mitsubishi",
          modelNumber = "MXZ-4C36NAHZ",
          serialNumber = "MS-2024-0012"
        ),
        issueTitle = "New 3-Zone Mini-Split Commissioning & Vacuum Hold",
        issueDescription = "500-micron deep vacuum decay test and nitrogen pressure test.",
        status = JobStatus.PENDING,
        priority = JobPriority.STANDARD,
        distanceMiles = 14.1,
        travelTimeMinutes = 28
      )
    )
  }

  private fun generateInitialAvailability(): Map<String, DayAvailability> {
    val map = mutableMapOf<String, DayAvailability>()
    val cal = Calendar.getInstance()
    // Set to 2026-08-31
    cal.set(2026, Calendar.AUGUST, 31)

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val monthNameFormat = SimpleDateFormat("MMMM", Locale.getDefault())

    for (i in 0..30) {
      val date = cal.time
      val key = dateFormat.format(date)
      val dayName = dayNameFormat.format(date)
      val dayNumber = cal.get(Calendar.DAY_OF_MONTH)
      val monthName = monthNameFormat.format(date)
      val isWeekend = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

      map[key] = DayAvailability(
        dateKey = key,
        dayName = dayName,
        dayNumber = dayNumber,
        monthName = monthName,
        isBlocked = isWeekend,
        blockReason = if (isWeekend) "Scheduled Day Off" else "",
        morningAvailable = !isWeekend,
        afternoonAvailable = !isWeekend,
        eveningAvailable = false,
        notes = if (key == "2026-08-31") "Ready for emergency dispatches" else ""
      )
      cal.add(Calendar.DAY_OF_MONTH, 1)
    }
    return map
  }
}
