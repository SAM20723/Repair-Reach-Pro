package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobItem
import com.example.ui.components.JobCard
import com.example.ui.components.JobStatusBadge
import com.example.ui.theme.ActionTeal
import com.example.ui.theme.BrandIndigo
import com.example.ui.theme.BrandTealPrimary
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.DividerLight
import com.example.ui.theme.PastelEmeraldBg
import com.example.ui.theme.PastelEmeraldText
import com.example.ui.theme.PastelSkyBg
import com.example.ui.theme.PastelSkyText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RepairViewModel

data class DateStripItem(
  val dateKey: String,
  val dayOfWeek: String,
  val dayOfMonth: Int,
  val monthName: String,
  val isToday: Boolean,
  val jobCount: Int
)

@Composable
fun ScheduleScreen(
  viewModel: RepairViewModel,
  modifier: Modifier = Modifier
) {
  val selectedDateKey by viewModel.selectedScheduleDateKey.collectAsState()
  val allJobs by viewModel.jobs.collectAsState()
  val scheduleJobs by viewModel.scheduleDateJobs.collectAsState()

  // Generate 7-day strip (Aug 31 to Sep 6)
  val dateStripList = listOf(
    DateStripItem("2026-08-31", "Mon", 31, "Aug", true, allJobs.count { it.scheduledDateKey == "2026-08-31" }),
    DateStripItem("2026-09-01", "Tue", 1, "Sep", false, allJobs.count { it.scheduledDateKey == "2026-09-01" }),
    DateStripItem("2026-09-02", "Wed", 2, "Sep", false, allJobs.count { it.scheduledDateKey == "2026-09-02" }),
    DateStripItem("2026-09-03", "Thu", 3, "Sep", false, allJobs.count { it.scheduledDateKey == "2026-09-03" }),
    DateStripItem("2026-09-04", "Fri", 4, "Sep", false, allJobs.count { it.scheduledDateKey == "2026-09-04" }),
    DateStripItem("2026-09-05", "Sat", 5, "Sep", false, allJobs.count { it.scheduledDateKey == "2026-09-05" }),
    DateStripItem("2026-09-06", "Sun", 6, "Sep", false, allJobs.count { it.scheduledDateKey == "2026-09-06" })
  )

  val activeDateItem = dateStripList.find { it.dateKey == selectedDateKey } ?: dateStripList[0]

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CanvasBackground)
      .testTag("schedule_screen")
  ) {
    // Header Bar
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = SurfaceCard,
      border = BorderStroke(1.dp, DividerLight)
    ) {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Service Schedule",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
            Text(
              text = "Late Summer Workload • Aug 31 – Sep 6, 2026",
              style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = SurfaceCardSubtle,
            border = BorderStroke(1.dp, DividerLight)
          ) {
            Row(
              modifier = Modifier
                .clickable { viewModel.setScheduleDate("2026-08-31") }
                .padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(Icons.Default.Today, contentDescription = null, tint = BrandTealPrimary, modifier = Modifier.size(14.dp))
              Text("Today", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandTealPrimary))
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Horizontal, Scrollable Date-Strip at top to select days
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .testTag("date_strip_scroll"),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          dateStripList.forEach { item ->
            val isSelected = item.dateKey == selectedDateKey
            Surface(
              modifier = Modifier
                .width(62.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable { viewModel.setScheduleDate(item.dateKey) }
                .testTag("date_strip_item_${item.dateKey}"),
              color = if (isSelected) BrandTealPrimary else SurfaceCardSubtle,
              border = BorderStroke(
                1.dp,
                if (isSelected) BrandTealPrimary else DividerLight
              ),
              shape = RoundedCornerShape(14.dp)
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Text(
                  text = item.dayOfWeek.uppercase(),
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else TextMuted
                  )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "${item.dayOfMonth}",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isSelected) Color.White else TextPrimary
                  )
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Dot indicators / badge for job count
                if (item.jobCount > 0) {
                  Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Color.White.copy(alpha = 0.25f) else PastelSkyBg,
                    modifier = Modifier.padding(top = 2.dp)
                  ) {
                    Text(
                      text = "${item.jobCount} jobs",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else PastelSkyText
                      ),
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                  }
                } else {
                  Box(
                    modifier = Modifier
                      .size(6.dp)
                      .clip(CircleShape)
                      .background(if (isSelected) Color.White.copy(alpha = 0.5f) else DividerLight)
                  )
                }
              }
            }
          }
        }
      }
    }

    // Selected Day Summary Banner
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = SurfaceCardSubtle,
      border = BorderStroke(1.dp, DividerLight)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(16.dp))
          Text(
            text = "${activeDateItem.dayOfWeek}, ${activeDateItem.monthName} ${activeDateItem.dayOfMonth}",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
        }

        val totalHours = scheduleJobs.sumOf { it.estimatedHours }
        Text(
          text = "${scheduleJobs.size} Jobs (${totalHours}h estimated)",
          style = MaterialTheme.typography.labelMedium.copy(
            color = TextSecondary,
            fontWeight = FontWeight.SemiBold
          )
        )
      }
    }

    // Below date-strip: Clean Timeline or List View of upcoming jobs
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("schedule_timeline_list"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      if (scheduleJobs.isEmpty()) {
        item {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = BorderStroke(1.dp, DividerLight)
          ) {
            Column(
              modifier = Modifier.padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Icon(Icons.Default.EventBusy, contentDescription = null, tint = TextMuted, modifier = Modifier.size(44.dp))
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "No Jobs Scheduled for this Day",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )
              Text(
                text = "Enjoy your open time slot or check your availability settings.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      } else {
        items(scheduleJobs, key = { it.id }) { job ->
          TimelineJobItem(
            job = job,
            onCardClick = { viewModel.openJobDetails(job.id) },
            onNavigateClick = { viewModel.triggerGpsNavigation(job) }
          )
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
fun TimelineJobItem(
  job: JobItem,
  onCardClick: () -> Unit,
  onNavigateClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Left Time Block Indicator
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.width(72.dp)
    ) {
      Text(
        text = job.scheduledTimeStart,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          color = BrandIndigo
        )
      )
      Text(
        text = job.scheduledTimeEnd,
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextMuted,
          fontSize = 11.sp
        )
      )
      Spacer(modifier = Modifier.height(6.dp))
      Box(
        modifier = Modifier
          .width(2.dp)
          .height(55.dp)
          .background(DividerLight)
      )
    }

    // Right Job Card
    Box(modifier = Modifier.weight(1f)) {
      JobCard(
        job = job,
        onClick = onCardClick,
        onNavigateClick = onNavigateClick
      )
    }
  }
}
