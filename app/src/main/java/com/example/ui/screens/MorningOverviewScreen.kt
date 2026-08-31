package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.components.MetricSummaryCard
import com.example.ui.theme.ActionTeal
import com.example.ui.theme.BrandTealPrimary
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.DividerLight
import com.example.ui.theme.PastelAmberBg
import com.example.ui.theme.PastelAmberText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.JobFilter
import com.example.ui.viewmodel.RepairViewModel

@Composable
fun MorningOverviewScreen(
  viewModel: RepairViewModel,
  modifier: Modifier = Modifier
) {
  val todayJobs by viewModel.todayJobs.collectAsState()
  val metrics by viewModel.metrics.collectAsState()
  val currentFilter by viewModel.todayFilter.collectAsState()
  val profile by viewModel.profile.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CanvasBackground)
      .testTag("morning_overview_screen")
  ) {
    // Header Greeting Bar
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = SurfaceCard,
      border = BorderStroke(1.dp, DividerLight)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.WbSunny,
              contentDescription = null,
              tint = PastelAmberText,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Good Morning, ${profile.name.substringBefore(" ")}",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
          }
          Text(
            text = "Monday, Aug 31 • Shift: 8:00 AM – 5:00 PM",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
          )
        }

        // Notification / Status Pill
        Surface(
          shape = CircleShape,
          color = SurfaceCardSubtle,
          border = BorderStroke(1.dp, DividerLight)
        ) {
          IconButton(
            onClick = { viewModel.showToast("No new urgent dispatch alerts.") },
            modifier = Modifier.size(40.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "Alerts",
              tint = TextSecondary,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Metric Summary Card
      item {
        MetricSummaryCard(
          totalJobs = metrics.totalJobsToday,
          completedJobs = metrics.completedJobsToday,
          capacityBooked = metrics.capacityHoursBooked,
          capacityTotal = metrics.capacityHoursTotal,
          onTimePercent = metrics.onTimePercentage
        )
      }

      // Filter Chips Section
      item {
        Column {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 4.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Today's Work Orders",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
            Text(
              text = "${todayJobs.size} Assigned",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextMuted,
                fontWeight = FontWeight.Medium
              )
            )
          }

          // Horizontal Filter Chips
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            JobFilter.values().forEach { filter ->
              val isSelected = filter == currentFilter
              FilterChip(
                selected = isSelected,
                onClick = { viewModel.setTodayFilter(filter) },
                label = {
                  Text(
                    text = filter.label,
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = BrandTealPrimary,
                  selectedLabelColor = Color.White,
                  containerColor = SurfaceCard,
                  labelColor = TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                  enabled = true,
                  selected = isSelected,
                  borderColor = if (isSelected) BrandTealPrimary else DividerLight
                )
              )
            }
          }
        }
      }

      // 2. Vertical list of today's assigned jobs displayed as elevated cards
      if (todayJobs.isEmpty()) {
        item {
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, DividerLight)
          ) {
            Column(
              modifier = Modifier.padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.Assignment,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(40.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "No jobs found for ${currentFilter.label}",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )
              Text(
                text = "Switch filter or check the schedule tab for upcoming days.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )
            }
          }
        }
      } else {
        items(todayJobs, key = { it.id }) { job ->
          JobCard(
            job = job,
            onClick = { viewModel.openJobDetails(job.id) },
            onNavigateClick = { viewModel.triggerGpsNavigation(job) }
          )
        }
      }

      // Bottom Spacer
      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
