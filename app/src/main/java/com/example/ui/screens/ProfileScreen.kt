package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TechnicianProfile
import com.example.ui.theme.ActionTeal
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
fun ProfileScreen(
  viewModel: RepairViewModel,
  modifier: Modifier = Modifier
) {
  val profile by viewModel.profile.collectAsState()
  var pushAlertsEnabled by remember { mutableStateOf(true) }
  var offlineSyncEnabled by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CanvasBackground)
      .testTag("profile_screen")
  ) {
    // Top Bar
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
            text = "Technician Profile",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
          Text(
            text = "ID: #${profile.id} • ${profile.company}",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = PastelEmeraldBg,
          border = BorderStroke(1.dp, PastelEmeraldBorder)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = PastelEmeraldText, modifier = Modifier.size(13.dp))
            Text("Active Duty", color = PastelEmeraldText, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
          }
        }
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Header with Avatar, Name, Trade, Certification
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard),
          border = BorderStroke(1.dp, DividerLight)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              // Technician Avatar Initials
              Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = BrandIndigo,
                border = BorderStroke(2.dp, ActionTeal)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = profile.avatarInitials,
                    style = MaterialTheme.typography.titleLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = Color.White,
                      fontSize = 22.sp
                    )
                  )
                }
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = profile.name,
                  style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                  )
                )
                Text(
                  text = profile.trade,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = ActionTeal
                  )
                )
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp),
                  modifier = Modifier.padding(top = 2.dp)
                ) {
                  Icon(Icons.Default.Star, contentDescription = null, tint = PastelAmberText, modifier = Modifier.size(15.dp))
                  Text(
                    text = "${profile.rating} (${profile.totalReviews} reviews)",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Certification Pill
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = PastelIndigoBg,
              border = BorderStroke(1.dp, PastelIndigoBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(Icons.Default.Badge, contentDescription = null, tint = PastelIndigoText, modifier = Modifier.size(15.dp))
                Text(
                  text = profile.certification,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PastelIndigoText
                  )
                )
              }
            }
          }
        }
      }

      // 2. "Weekly Capacity" Progress Bar (Hours Booked vs Available)
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("weekly_capacity_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = SurfaceCard),
          border = BorderStroke(1.dp, DividerLight)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Weekly Capacity & Utilization",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )
              val utilization = ((profile.weeklyBookedHours / profile.weeklyCapacityHours) * 100).toInt()
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (utilization <= 85) PastelEmeraldBg else PastelAmberBg,
                border = BorderStroke(1.dp, if (utilization <= 85) PastelEmeraldBorder else PastelAmberBorder)
              ) {
                Text(
                  text = "$utilization% Capacity",
                  color = if (utilization <= 85) PastelEmeraldText else PastelAmberText,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hours indicator
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              Row(verticalAlignment = Alignment.Bottom) {
                Text(
                  text = "${profile.weeklyBookedHours}",
                  style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = BrandIndigo
                  )
                )
                Text(
                  text = " / ${profile.weeklyCapacityHours} Hours Booked",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                  ),
                  modifier = Modifier.padding(bottom = 3.dp, start = 4.dp)
                )
              }

              Text(
                text = "${profile.weeklyCapacityHours - profile.weeklyBookedHours}h remaining",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = TextMuted,
                  fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(bottom = 3.dp)
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Capacity Progress Bar
            val capacityRatio = (profile.weeklyBookedHours / profile.weeklyCapacityHours).toFloat().coerceIn(0f, 1f)
            LinearProgressIndicator(
              progress = { capacityRatio },
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(6.dp)),
              color = if (capacityRatio > 0.85f) Color(0xFFD97706) else ActionTeal,
              trackColor = DividerLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Weekly Highlights Summary
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              WeeklyStatMini(label = "Jobs Finished", value = "${profile.weeklyCompletedJobs}")
              WeeklyStatMini(label = "First-Time Fix", value = "${profile.firstTimeFixPercent}%")
              WeeklyStatMini(label = "Assigned Van", value = profile.vehicleVan)
            }
          }
        }
      }

      // 3. Settings List Section
      item {
        Text(
          text = "Preferences & Dispatch Settings",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          ),
          modifier = Modifier.padding(top = 4.dp)
        )
      }

      // Setting: Account & Credentials
      item {
        SettingsItemCard(
          icon = Icons.Default.Person,
          title = "Account & Licenses",
          subtitle = "EPA Universal, NATE HVAC Certifications",
          onClick = { viewModel.showToast("Account & Licenses: Verified Active") }
        )
      }

      // Setting: Support & Dispatch Line
      item {
        SettingsItemCard(
          icon = Icons.Default.HeadsetMic,
          title = "Direct Dispatch Support",
          subtitle = "Central dispatch coordinator (Austin Region)",
          onClick = { viewModel.showToast("Connecting to Austin Dispatch Hotline: (555) 019-2830") }
        )
      }

      // Setting: Push Notifications Toggle
      item {
        SettingsSwitchCard(
          icon = Icons.Default.NotificationsActive,
          title = "Push & Urgent Alerts",
          subtitle = "Emergency work orders & schedule adjustments",
          checked = pushAlertsEnabled,
          onCheckedChange = {
            pushAlertsEnabled = it
            viewModel.showToast(if (it) "Urgent alerts enabled" else "Urgent alerts muted")
          }
        )
      }

      // Setting: Offline Sync Mode
      item {
        SettingsSwitchCard(
          icon = Icons.Default.WifiOff,
          title = "Offline Field Mode",
          subtitle = "Cache job forms and schematics locally",
          checked = offlineSyncEnabled,
          onCheckedChange = {
            offlineSyncEnabled = it
            viewModel.showToast(if (it) "Offline mode enabled. Schematics cached locally." else "Live online sync enabled.")
          }
        )
      }

      // 4. Subtle "Sign Out" button at the very bottom
      item {
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
          onClick = { viewModel.showToast("Logged out of RepairReach Pro. Shift session ended.") },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("btn_sign_out"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted),
          border = BorderStroke(1.dp, DividerLight)
        ) {
          Icon(Icons.Default.Logout, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Sign Out (End Shift)",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.SemiBold,
              color = TextSecondary
            )
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
fun WeeklyStatMini(label: String, value: String) {
  Column(
    modifier = Modifier
      .clip(RoundedCornerShape(10.dp))
      .background(SurfaceCardSubtle)
      .padding(horizontal = 10.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = TextPrimary
      )
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(
        color = TextMuted,
        fontSize = 10.sp
      )
    )
  }
}

@Composable
fun SettingsItemCard(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = BorderStroke(1.dp, DividerLight)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
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
          color = SurfaceCardSubtle,
          border = BorderStroke(1.dp, DividerLight),
          modifier = Modifier.size(40.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(20.dp))
          }
        }
        Column {
          Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
          Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
        }
      }
      Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
    }
  }
}

@Composable
fun SettingsSwitchCard(
  icon: ImageVector,
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = BorderStroke(1.dp, DividerLight)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
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
          color = SurfaceCardSubtle,
          border = BorderStroke(1.dp, DividerLight),
          modifier = Modifier.size(40.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(20.dp))
          }
        }
        Column {
          Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
          Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
        }
      }
      Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
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
