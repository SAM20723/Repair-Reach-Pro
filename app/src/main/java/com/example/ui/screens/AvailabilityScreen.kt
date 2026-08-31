package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DayAvailability
import com.example.data.model.ShiftSlot
import com.example.ui.theme.ActionTeal
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandIndigo
import com.example.ui.theme.BrandTealPrimary
import com.example.ui.theme.CanvasBackground
import com.example.ui.theme.DividerLight
import com.example.ui.theme.PastelAmberBg
import com.example.ui.theme.PastelAmberBorder
import com.example.ui.theme.PastelAmberText
import com.example.ui.theme.PastelEmeraldBg
import com.example.ui.theme.PastelEmeraldBorder
import com.example.ui.theme.PastelEmeraldText
import com.example.ui.theme.PastelIndigoBg
import com.example.ui.theme.PastelIndigoBorder
import com.example.ui.theme.PastelIndigoText
import com.example.ui.theme.PastelRoseBg
import com.example.ui.theme.PastelRoseBorder
import com.example.ui.theme.PastelRoseText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RepairViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailabilityScreen(
  viewModel: RepairViewModel,
  modifier: Modifier = Modifier
) {
  val selectedDateKey by viewModel.selectedAvailabilityDateKey.collectAsState()
  val availabilityMap by viewModel.availabilityMap.collectAsState()
  val currentAvailability by viewModel.selectedDayAvailability.collectAsState()

  val selectedDay = currentAvailability ?: DayAvailability(
    dateKey = selectedDateKey,
    dayName = "Mon",
    dayNumber = 31,
    monthName = "August",
    isBlocked = false,
    morningAvailable = true,
    afternoonAvailable = true,
    eveningAvailable = false
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CanvasBackground)
      .testTag("availability_screen")
  ) {
    // Header Bar
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
          Text(
            text = "Managing Time",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
          Text(
            text = "Set Working Hours & Shift Availability",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
          )
        }

        Surface(
          shape = RoundedCornerShape(20.dp),
          color = PastelIndigoBg,
          border = BorderStroke(1.dp, PastelIndigoBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = PastelIndigoText, modifier = Modifier.size(13.dp))
            Text(
              text = "Live Sync",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = PastelIndigoText)
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
      // 1. Calendar Month-Picker
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("month_picker_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard),
          border = BorderStroke(1.dp, DividerLight)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Month Header with Prev/Next buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(20.dp))
                Text(
                  text = "September 2026",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                )
              }

              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Surface(
                  shape = CircleShape,
                  color = SurfaceCardSubtle,
                  border = BorderStroke(1.dp, DividerLight)
                ) {
                  IconButton(
                    onClick = { viewModel.showToast("Showing August/September 2026") },
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month", tint = TextSecondary, modifier = Modifier.size(18.dp))
                  }
                }
                Surface(
                  shape = CircleShape,
                  color = SurfaceCardSubtle,
                  border = BorderStroke(1.dp, DividerLight)
                ) {
                  IconButton(
                    onClick = { viewModel.showToast("Showing September/October 2026") },
                    modifier = Modifier.size(32.dp)
                  ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next Month", tint = TextSecondary, modifier = Modifier.size(18.dp))
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Day-of-week header row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              listOf("M", "T", "W", "T", "F", "S", "S").forEach { dayLabel ->
                Text(
                  text = dayLabel,
                  modifier = Modifier.weight(1f),
                  textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextMuted
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Interactive Calendar Grid (Days 31 Aug to 20 Sep)
            val daysInCalendar = listOf(
              CalendarDay("2026-08-31", 31, isCurrentMonth = true),
              CalendarDay("2026-09-01", 1, isCurrentMonth = true),
              CalendarDay("2026-09-02", 2, isCurrentMonth = true),
              CalendarDay("2026-09-03", 3, isCurrentMonth = true),
              CalendarDay("2026-09-04", 4, isCurrentMonth = true),
              CalendarDay("2026-09-05", 5, isCurrentMonth = true),
              CalendarDay("2026-09-06", 6, isCurrentMonth = true),
              CalendarDay("2026-09-07", 7, isCurrentMonth = true),
              CalendarDay("2026-09-08", 8, isCurrentMonth = true),
              CalendarDay("2026-09-09", 9, isCurrentMonth = true),
              CalendarDay("2026-09-10", 10, isCurrentMonth = true),
              CalendarDay("2026-09-11", 11, isCurrentMonth = true),
              CalendarDay("2026-09-12", 12, isCurrentMonth = true),
              CalendarDay("2026-09-13", 13, isCurrentMonth = true)
            )

            // Chunk in rows of 7
            daysInCalendar.chunked(7).forEach { weekRow ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                weekRow.forEach { day ->
                  val isSelected = day.dateKey == selectedDateKey
                  val availability = availabilityMap[day.dateKey]
                  val isBlocked = availability?.isBlocked == true

                  Surface(
                    modifier = Modifier
                      .weight(1f)
                      .aspectRatio(1f)
                      .padding(2.dp)
                      .clip(CircleShape)
                      .clickable { viewModel.setAvailabilityDate(day.dateKey) }
                      .testTag("calendar_day_${day.dateKey}"),
                    shape = CircleShape,
                    color = if (isSelected) BrandTealPrimary
                    else if (isBlocked) PastelRoseBg
                    else SurfaceCardSubtle,
                    border = BorderStroke(
                      1.dp,
                      if (isSelected) BrandTealPrimary
                      else if (isBlocked) PastelRoseBorder
                      else DividerLight
                    )
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                      ) {
                        Text(
                          text = "${day.dayNumber}",
                          style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White
                            else if (isBlocked) PastelRoseText
                            else TextPrimary
                          )
                        )
                        if (isBlocked) {
                          Box(
                            modifier = Modifier
                              .size(4.dp)
                              .clip(CircleShape)
                              .background(if (isSelected) Color.White else PastelRoseText)
                          )
                        }
                      }
                    }
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Calendar Legend
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(BrandTealPrimary))
                Text("Selected", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              }
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(PastelEmeraldBg).border(1.dp, PastelEmeraldBorder, CircleShape))
                Text("Available", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              }
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(PastelRoseBg).border(1.dp, PastelRoseBorder, CircleShape))
                Text("Blocked / Off", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
              }
            }
          }
        }
      }

      // 2. TOGGLE SWITCH: "Block Entire Day" (for time off / PTO)
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("block_day_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (selectedDay.isBlocked) PastelRoseBg else SurfaceCard
          ),
          border = BorderStroke(
            1.dp,
            if (selectedDay.isBlocked) PastelRoseBorder else DividerLight
          )
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
              ) {
                Surface(
                  shape = CircleShape,
                  color = if (selectedDay.isBlocked) PastelRoseText.copy(alpha = 0.12f) else SurfaceCardSubtle,
                  modifier = Modifier.size(40.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Icon(
                      imageVector = if (selectedDay.isBlocked) Icons.Default.EventBusy else Icons.Default.Block,
                      contentDescription = null,
                      tint = if (selectedDay.isBlocked) PastelRoseText else TextSecondary,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                }

                Column {
                  Text(
                    text = "Block Entire Day",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = if (selectedDay.isBlocked) PastelRoseText else TextPrimary
                    )
                  )
                  Text(
                    text = if (selectedDay.isBlocked) "No shifts assigned for this date" else "Request full day off or mark unavailable",
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = if (selectedDay.isBlocked) PastelRoseText.copy(alpha = 0.8f) else TextMuted
                    )
                  )
                }
              }

              Switch(
                checked = selectedDay.isBlocked,
                onCheckedChange = { isChecked ->
                  viewModel.toggleDayBlocked(selectedDay.dateKey, isChecked, selectedDay.blockReason.ifEmpty { "Time Off / PTO" })
                },
                modifier = Modifier.testTag("toggle_block_entire_day"),
                colors = SwitchDefaults.colors(
                  checkedThumbColor = Color.White,
                  checkedTrackColor = PastelRoseText,
                  uncheckedThumbColor = TextMuted,
                  uncheckedTrackColor = DividerLight
                )
              )
            }

            // If day is blocked, allow picking reason
            AnimatedVisibility(visible = selectedDay.isBlocked) {
              Column(modifier = Modifier.padding(top = 12.dp)) {
                Text(
                  text = "Time-off Reason",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PastelRoseText
                  )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  listOf("Time Off / PTO", "Sick Leave", "Training", "Personal").forEach { reason ->
                    val isSelected = selectedDay.blockReason == reason
                    Surface(
                      modifier = Modifier
                        .clickable { viewModel.toggleDayBlocked(selectedDay.dateKey, true, reason) },
                      shape = RoundedCornerShape(8.dp),
                      color = if (isSelected) PastelRoseText else Color.White,
                      border = BorderStroke(1.dp, if (isSelected) PastelRoseText else PastelRoseBorder)
                    ) {
                      Text(
                        text = reason,
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontWeight = FontWeight.Bold,
                          color = if (isSelected) Color.White else PastelRoseText,
                          fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }

      // 3. If day is NOT blocked: Display standard shift blocks with toggle switches
      if (!selectedDay.isBlocked) {
        item {
          Text(
            text = "Standard Shift Blocks (${selectedDay.dayName}, ${selectedDay.monthName} ${selectedDay.dayNumber})",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            ),
            modifier = Modifier.padding(top = 4.dp)
          )
        }

        // Shift 1: Morning 9am - 1pm
        item {
          ShiftBlockCard(
            title = "Morning Shift",
            timeRange = "9:00 AM – 1:00 PM",
            duration = "4.0 Hrs",
            icon = Icons.Default.WbSunny,
            isAvailable = selectedDay.morningAvailable,
            onToggle = { isChecked ->
              viewModel.toggleShift(selectedDay.dateKey, ShiftSlot.MORNING, isChecked)
            }
          )
        }

        // Shift 2: Afternoon 1pm - 5pm
        item {
          ShiftBlockCard(
            title = "Afternoon Shift",
            timeRange = "1:00 PM – 5:00 PM",
            duration = "4.0 Hrs",
            icon = Icons.Default.WbTwilight,
            isAvailable = selectedDay.afternoonAvailable,
            onToggle = { isChecked ->
              viewModel.toggleShift(selectedDay.dateKey, ShiftSlot.AFTERNOON, isChecked)
            }
          )
        }

        // Shift 3: Evening 5pm - 8pm
        item {
          ShiftBlockCard(
            title = "Evening Shift (On-Call)",
            timeRange = "5:00 PM – 8:00 PM",
            duration = "3.0 Hrs",
            icon = Icons.Default.NightsStay,
            isAvailable = selectedDay.eveningAvailable,
            onToggle = { isChecked ->
              viewModel.toggleShift(selectedDay.dateKey, ShiftSlot.EVENING, isChecked)
            }
          )
        }

        // Quick Apply Action
        item {
          Button(
            onClick = {
              viewModel.showToast("Applied standard shifts (9 AM - 5 PM) to all weekdays!")
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("btn_apply_template"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandTealPrimary)
          ) {
            Icon(Icons.Default.DoneAll, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Apply Standard Shift to All Weekdays",
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

private data class CalendarDay(val dateKey: String, val dayNumber: Int, val isCurrentMonth: Boolean)

@Composable
fun ShiftBlockCard(
  title: String,
  timeRange: String,
  duration: String,
  icon: ImageVector,
  isAvailable: Boolean,
  onToggle: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = BorderStroke(
      1.dp,
      if (isAvailable) BrandTealPrimary else DividerLight
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.weight(1f)
      ) {
        Surface(
          shape = CircleShape,
          color = if (isAvailable) PastelEmeraldBg else SurfaceCardSubtle,
          border = BorderStroke(1.dp, if (isAvailable) PastelEmeraldBorder else DividerLight),
          modifier = Modifier.size(44.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = if (isAvailable) PastelEmeraldText else TextMuted,
              modifier = Modifier.size(22.dp)
            )
          }
        }

        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = if (isAvailable) PastelEmeraldBg else SurfaceCardSubtle,
              border = BorderStroke(1.dp, if (isAvailable) PastelEmeraldBorder else DividerLight)
            ) {
              Text(
                text = if (isAvailable) "Available" else "Off",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = if (isAvailable) PastelEmeraldText else TextMuted,
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                ),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "$timeRange • $duration",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextSecondary,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }

      Switch(
        checked = isAvailable,
        onCheckedChange = onToggle,
        modifier = Modifier.testTag("switch_shift_${title.lowercase().replace(" ", "_")}"),
        colors = SwitchDefaults.colors(
          checkedThumbColor = Color.White,
          checkedTrackColor = ActionTeal,
          uncheckedThumbColor = TextMuted,
          uncheckedTrackColor = DividerLight
        )
      )
    }
  }
}
