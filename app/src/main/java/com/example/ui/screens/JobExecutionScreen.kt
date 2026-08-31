package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobItem
import com.example.data.model.JobStatus
import com.example.ui.components.CustomerActionRow
import com.example.ui.components.JobStatusBadge
import com.example.ui.components.MapDestinationView
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StickyLifecycleActionBar
import com.example.ui.theme.ActionTeal
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandIndigo
import com.example.ui.theme.BrandTealPrimary
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.DividerLight
import com.example.ui.theme.PastelEmeraldBg
import com.example.ui.theme.PastelEmeraldBorder
import com.example.ui.theme.PastelEmeraldText
import com.example.ui.theme.PastelIndigoBg
import com.example.ui.theme.PastelIndigoBorder
import com.example.ui.theme.PastelIndigoText
import com.example.ui.theme.PastelPurpleBg
import com.example.ui.theme.PastelPurpleBorder
import com.example.ui.theme.PastelPurpleText
import com.example.ui.theme.PastelSkyBg
import com.example.ui.theme.PastelSkyBorder
import com.example.ui.theme.PastelSkyText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RepairViewModel

@Composable
fun JobExecutionScreen(
  viewModel: RepairViewModel,
  modifier: Modifier = Modifier
) {
  val activeJob by viewModel.activeExecutingJob.collectAsState()
  val allJobs by viewModel.jobs.collectAsState()

  if (activeJob == null) {
    Box(
      modifier = modifier
        .fillMaxSize()
        .background(CanvasBackground),
      contentAlignment = Alignment.Center
    ) {
      Text("No active job selected. Please select a job from the Today screen.")
    }
    return
  }

  val job = activeJob!!

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CanvasBackground)
      .testTag("job_execution_screen")
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Navigation / Job Switcher Bar
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceCard,
        border = BorderStroke(1.dp, DividerLight)
      ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = job.workOrderNumber,
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BrandIndigo
                  )
                )
                PriorityBadge(priority = job.priority)
              }
              Text(
                text = "${job.scheduledDateDisplay} • ${job.scheduledTimeStart}",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
              )
            }

            JobStatusBadge(status = job.status)
          }

          // Job Quick Switcher (when multiple jobs exist today)
          val todayJobs = allJobs.filter { it.scheduledDateKey == viewModel.todayDateKey }
          if (todayJobs.size > 1) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              todayJobs.forEach { item ->
                val isSelected = item.id == job.id
                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { viewModel.openJobDetails(item.id) },
                  color = if (isSelected) BrandTealPrimary else SurfaceCardSubtle,
                  border = BorderStroke(1.dp, if (isSelected) BrandTealPrimary else DividerLight),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = "${item.workOrderNumber.replace("WO-", "#")} • ${item.customer.name.substringBefore(" ")}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                      color = if (isSelected) Color.White else TextSecondary
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }
      }

      // Scrollable Job Details Content
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // 1. TOP SECTION: Map Placeholder showing destination & navigation
        item {
          MapDestinationView(
            job = job,
            onNavigateClick = { viewModel.triggerGpsNavigation(job) }
          )
        }

        // 2. MIDDLE SECTION: Customer Details with Circular Quick-Action buttons
        item {
          Text(
            text = "Customer & Site Contact",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
          Spacer(modifier = Modifier.height(6.dp))
          CustomerActionRow(
            customer = job.customer,
            onCallClick = { viewModel.openCallDialog(job) },
            onMessageClick = { viewModel.openMessageDialog(job) }
          )
        }

        // 3. Equipment & Problem Description
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = BorderStroke(1.dp, DividerLight)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(Icons.Default.Build, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(18.dp))
                  Text(
                    text = "Equipment & Diagnostic Info",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = TextPrimary
                    )
                  )
                }
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = PastelEmeraldBg,
                  border = BorderStroke(1.dp, PastelEmeraldBorder)
                ) {
                  Text(
                    text = job.equipment.warrantyStatus,
                    color = PastelEmeraldText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = job.equipment.name,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )

              Spacer(modifier = Modifier.height(6.dp))

              // Specs Grid
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text("Model Number", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                  Text(job.equipment.modelNumber, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary))
                }
                Column(modifier = Modifier.weight(1f)) {
                  Text("Serial Number", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                  Text(job.equipment.serialNumber, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = TextPrimary))
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text("Unit Location", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              Text(job.equipment.locationInBuilding, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = TextSecondary))

              Spacer(modifier = Modifier.height(12.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(1.dp)
                  .background(DividerLight)
              )
              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = "Reported Symptoms & Issue",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp,
                  color = TextMuted
                )
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = job.issueTitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )
              Text(
                text = job.issueDescription,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = TextSecondary,
                  lineHeight = 18.sp
                ),
                modifier = Modifier.padding(top = 2.dp)
              )
            }
          }
        }

        // 4. Interactive Field Checklist
        if (job.checklist.isNotEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = SurfaceCard),
              border = BorderStroke(1.dp, DividerLight)
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                val completedCount = job.checklist.count { it.isCompleted }
                val totalCount = job.checklist.size

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Icon(Icons.Default.Checklist, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(18.dp))
                    Text(
                      text = "Service Checklist",
                      style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                      )
                    )
                  }

                  Text(
                    text = "$completedCount of $totalCount Done",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = if (completedCount == totalCount) PastelEmeraldText else TextMuted,
                      fontWeight = FontWeight.Bold
                    )
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                job.checklist.forEach { checkItem ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(8.dp))
                      .clickable { viewModel.toggleChecklist(job.id, checkItem.id) }
                      .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Checkbox(
                      checked = checkItem.isCompleted,
                      onCheckedChange = { viewModel.toggleChecklist(job.id, checkItem.id) },
                      colors = CheckboxDefaults.colors(
                        checkedColor = ActionTeal,
                        uncheckedColor = BorderSubtle
                      )
                    )
                    Text(
                      text = checkItem.title,
                      style = MaterialTheme.typography.bodySmall.copy(
                        color = if (checkItem.isCompleted) TextMuted else TextPrimary,
                        fontWeight = if (checkItem.isCompleted) FontWeight.Normal else FontWeight.Medium
                      ),
                      modifier = Modifier.weight(1f)
                    )
                  }
                }
              }
            }
          }
        }

        // 5. Work Order Photo Verification & Notes
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            border = BorderStroke(1.dp, DividerLight)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(18.dp))
                Text(
                  text = "Field Photo Attachments",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                )
              }
              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                // Before Photo Box
                Surface(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.togglePhoto(job.id, isAfter = false) },
                  color = if (job.beforePhotoCaptured) PastelEmeraldBg else SurfaceCardSubtle,
                  border = BorderStroke(1.dp, if (job.beforePhotoCaptured) PastelEmeraldBorder else DividerLight),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Icon(
                      imageVector = if (job.beforePhotoCaptured) Icons.Default.CheckCircle else Icons.Default.AddAPhoto,
                      contentDescription = null,
                      tint = if (job.beforePhotoCaptured) PastelEmeraldText else TextSecondary,
                      modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = if (job.beforePhotoCaptured) "Before Photo ✓" else "Add Before Photo",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (job.beforePhotoCaptured) PastelEmeraldText else TextPrimary
                      )
                    )
                  }
                }

                // After Photo Box
                Surface(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { viewModel.togglePhoto(job.id, isAfter = true) },
                  color = if (job.afterPhotoCaptured) PastelEmeraldBg else SurfaceCardSubtle,
                  border = BorderStroke(1.dp, if (job.afterPhotoCaptured) PastelEmeraldBorder else DividerLight),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Icon(
                      imageVector = if (job.afterPhotoCaptured) Icons.Default.CheckCircle else Icons.Default.AddAPhoto,
                      contentDescription = null,
                      tint = if (job.afterPhotoCaptured) PastelEmeraldText else TextSecondary,
                      modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = if (job.afterPhotoCaptured) "After Photo ✓" else "Add After Photo",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (job.afterPhotoCaptured) PastelEmeraldText else TextPrimary
                      )
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Technician notes
              Text(
                text = "Technician Work Order Notes",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextMuted
                )
              )
              Spacer(modifier = Modifier.height(4.dp))
              var notesText by remember(job.id, job.techNotes) { mutableStateOf(job.techNotes) }
              OutlinedTextField(
                value = notesText,
                onValueChange = {
                  notesText = it
                  viewModel.updateNotes(job.id, it)
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("tech_notes_input"),
                placeholder = { Text("Enter field diagnostic notes, readings, or parts installed...", style = MaterialTheme.typography.bodySmall) },
                textStyle = MaterialTheme.typography.bodySmall.copy(color = TextPrimary),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = ActionTeal,
                  unfocusedBorderColor = DividerLight,
                  focusedContainerColor = Color.White,
                  unfocusedContainerColor = SurfaceCardSubtle
                ),
                shape = RoundedCornerShape(10.dp),
                minLines = 2
              )
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(80.dp)) // Padding for sticky bottom bar
        }
      }
    }

    // 3. BOTTOM SECTION: Sticky bottom bar with full-width state-machine button
    StickyLifecycleActionBar(
      status = job.status,
      onActionClick = { viewModel.advanceSelectedJobLifecycle(job.id) },
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}
