package com.example.data.model

enum class JobStatus(val displayName: String) {
  PENDING("Pending"),
  EN_ROUTE("En Route"),
  IN_PROGRESS("In Progress"),
  COMPLETED("Completed")
}

enum class JobPriority(val label: String) {
  LOW("Low"),
  STANDARD("Standard"),
  HIGH("High"),
  EMERGENCY("Emergency")
}

data class Customer(
  val name: String,
  val phone: String,
  val address: String,
  val unitOrSuite: String = "",
  val propertyType: String = "Residential",
  val gateCode: String = "",
  val accessNotes: String = ""
)

data class Equipment(
  val name: String,
  val brand: String,
  val modelNumber: String,
  val serialNumber: String,
  val installYear: String = "2021",
  val locationInBuilding: String = "Rooftop / Mechanical Room",
  val warrantyStatus: String = "Active (Parts & Labor)"
)

data class ChecklistItem(
  val id: String,
  val title: String,
  val isCompleted: Boolean = false,
  val required: Boolean = true
)

data class PartUsed(
  val id: String,
  val name: String,
  val partNumber: String,
  val quantity: Int,
  val unitCost: Double
)

data class JobItem(
  val id: String,
  val workOrderNumber: String,
  val scheduledDateKey: String, // e.g. "2026-08-31", "2026-09-01"
  val scheduledDateDisplay: String, // e.g. "Today, Aug 31"
  val scheduledTimeStart: String,
  val scheduledTimeEnd: String,
  val estimatedHours: Double,
  val customer: Customer,
  val equipment: Equipment,
  val issueTitle: String,
  val issueDescription: String,
  val status: JobStatus,
  val priority: JobPriority,
  val distanceMiles: Double,
  val travelTimeMinutes: Int,
  val checklist: List<ChecklistItem> = emptyList(),
  val partsUsed: List<PartUsed> = emptyList(),
  val techNotes: String = "",
  val invoiceAmount: Double = 285.00,
  val completionTimestamp: String? = null,
  val beforePhotoCaptured: Boolean = false,
  val afterPhotoCaptured: Boolean = false
)

enum class ShiftSlot(val label: String, val timeRange: String, val hours: Double) {
  MORNING("Morning", "9:00 AM – 1:00 PM", 4.0),
  AFTERNOON("Afternoon", "1:00 PM – 5:00 PM", 4.0),
  EVENING("Evening", "5:00 PM – 8:00 PM", 3.0)
}

data class DayAvailability(
  val dateKey: String, // "2026-08-31"
  val dayName: String, // "Mon", "Tue", etc.
  val dayNumber: Int,
  val monthName: String,
  val isBlocked: Boolean = false,
  val blockReason: String = "Requested Off",
  val morningAvailable: Boolean = true,
  val afternoonAvailable: Boolean = true,
  val eveningAvailable: Boolean = false,
  val notes: String = ""
)

data class TechnicianProfile(
  val id: String = "RR-8492",
  val name: String = "Marcus Vance",
  val trade: String = "Senior HVAC & Climate Tech",
  val company: String = "Apex Mechanical Services",
  val certification: String = "NATE & EPA Universal Master Certified",
  val rating: Double = 4.95,
  val totalReviews: Int = 342,
  val avatarInitials: String = "MV",
  val vehicleVan: String = "Ford Transit #402",
  val weeklyCapacityHours: Double = 40.0,
  val weeklyBookedHours: Double = 28.5,
  val weeklyCompletedJobs: Int = 14,
  val onTimeRatePercent: Int = 98,
  val firstTimeFixPercent: Int = 94,
  val emergencyOnCall: Boolean = true
)
