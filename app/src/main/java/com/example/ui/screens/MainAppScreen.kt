package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobItem
import com.example.data.model.JobStatus
import com.example.ui.theme.ActionTeal
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
import com.example.ui.theme.PastelSkyBg
import com.example.ui.theme.PastelSkyBorder
import com.example.ui.theme.PastelSkyText
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RepairTab
import com.example.ui.viewmodel.RepairViewModel

data class NavTabItem(
  val tab: RepairTab,
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
)

@Composable
fun MainAppScreen(
  viewModel: RepairViewModel,
  modifier: Modifier = Modifier
) {
  val currentTab by viewModel.currentTab.collectAsState()
  val activeJob by viewModel.activeExecutingJob.collectAsState()
  val toastMessage by viewModel.toastMessage.collectAsState()

  val showCallDialogJob by viewModel.showCallDialog.collectAsState()
  val showMessageDialogJob by viewModel.showMessageDialog.collectAsState()
  val showCompleteSummaryJob by viewModel.showCompleteSummaryDialog.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(toastMessage) {
    toastMessage?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearToast()
    }
  }

  val navItems = listOf(
    NavTabItem(RepairTab.TODAY, "Today", Icons.Filled.Assignment, Icons.Outlined.Assignment),
    NavTabItem(RepairTab.EXECUTION, "Job Details", Icons.Filled.Build, Icons.Outlined.Build),
    NavTabItem(RepairTab.SCHEDULE, "Schedule", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth),
    NavTabItem(RepairTab.AVAILABILITY, "Availability", Icons.Filled.EventAvailable, Icons.Outlined.EventAvailable),
    NavTabItem(RepairTab.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
  )

  Scaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    bottomBar = {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(8.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, DividerLight)
      ) {
        NavigationBar(
          modifier = Modifier
            .navigationBarsPadding()
            .testTag("main_bottom_nav"),
          containerColor = SurfaceCard,
          contentColor = TextSecondary,
          tonalElevation = 0.dp
        ) {
          navItems.forEach { item ->
            val isSelected = currentTab == item.tab
            val hasActiveBadge = item.tab == RepairTab.EXECUTION && activeJob?.status == JobStatus.IN_PROGRESS

            NavigationBarItem(
              selected = isSelected,
              onClick = { viewModel.selectTab(item.tab) },
              modifier = Modifier.testTag("nav_tab_${item.tab.name.lowercase()}"),
              icon = {
                if (hasActiveBadge) {
                  BadgedBox(
                    badge = {
                      Badge(
                        containerColor = ActionTeal,
                        contentColor = Color.White
                      ) {
                        Text("1", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
                      }
                    }
                  ) {
                    Icon(
                      imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                      contentDescription = item.label,
                      tint = if (isSelected) BrandTealPrimary else TextMuted
                    )
                  }
                } else {
                  Icon(
                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = item.label,
                    tint = if (isSelected) BrandTealPrimary else TextMuted
                  )
                }
              },
              label = {
                Text(
                  text = item.label,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 11.sp,
                    color = if (isSelected) BrandTealPrimary else TextMuted
                  )
                )
              },
              colors = NavigationBarItemDefaults.colors(
                indicatorColor = PastelSkyBg,
                selectedIconColor = BrandTealPrimary,
                selectedTextColor = BrandTealPrimary,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
              )
            )
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = currentTab,
        transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(100)) },
        label = "TabTransition"
      ) { targetTab ->
        when (targetTab) {
          RepairTab.TODAY -> MorningOverviewScreen(viewModel = viewModel)
          RepairTab.EXECUTION -> JobExecutionScreen(viewModel = viewModel)
          RepairTab.SCHEDULE -> ScheduleScreen(viewModel = viewModel)
          RepairTab.AVAILABILITY -> AvailabilityScreen(viewModel = viewModel)
          RepairTab.PROFILE -> ProfileScreen(viewModel = viewModel)
        }
      }
    }
  }

  // --- DIALOGS ---

  // 1. Quick Call Confirmation Dialog
  showCallDialogJob?.let { job ->
    AlertDialog(
      onDismissRequest = { viewModel.closeCallDialog() },
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = PastelEmeraldBg,
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.Call, contentDescription = null, tint = PastelEmeraldText, modifier = Modifier.size(18.dp))
            }
          }
          Text("Call Customer", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
      },
      text = {
        Column {
          Text("Connect directly with:", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
          Text(job.customer.name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
          Text(job.customer.phone, style = MaterialTheme.typography.bodyLarge.copy(color = BrandIndigo, fontWeight = FontWeight.Bold))
          if (job.customer.gateCode.isNotEmpty()) {
            Text("Gate Code: ${job.customer.gateCode}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { viewModel.triggerCall(job) },
          colors = ButtonDefaults.buttonColors(containerColor = BrandTealPrimary),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Place Call")
        }
      },
      dismissButton = {
        OutlinedButton(
          onClick = { viewModel.closeCallDialog() },
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Cancel")
        }
      },
      containerColor = SurfaceCard,
      shape = RoundedCornerShape(16.dp)
    )
  }

  // 2. Preset Quick SMS Dialog
  showMessageDialogJob?.let { job ->
    AlertDialog(
      onDismissRequest = { viewModel.closeMessageDialog() },
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = PastelIndigoBg,
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.Message, contentDescription = null, tint = PastelIndigoText, modifier = Modifier.size(18.dp))
            }
          }
          Text("Quick Message to Customer", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Select a 1-tap message template for ${job.customer.name}:", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))

          val templates = listOf(
            "I'm on my way! Estimated arrival in 15 mins.",
            "I have arrived outside and parked the service van.",
            "Running 10 mins late due to traffic. See you shortly!",
            "Please ensure the equipment room/gate is unlocked."
          )

          templates.forEach { msg ->
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { viewModel.sendPresetMessage(job, msg) },
              color = SurfaceCardSubtle,
              border = BorderStroke(1.dp, DividerLight),
              shape = RoundedCornerShape(8.dp)
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = msg,
                  style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.Medium),
                  modifier = Modifier.weight(1f)
                )
                Icon(Icons.Default.Send, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(16.dp))
              }
            }
          }
        }
      },
      confirmButton = {
        OutlinedButton(
          onClick = { viewModel.closeMessageDialog() },
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Close")
        }
      },
      containerColor = SurfaceCard,
      shape = RoundedCornerShape(16.dp)
    )
  }

  // 3. Job Completed Summary Dialog
  showCompleteSummaryJob?.let { job ->
    AlertDialog(
      onDismissRequest = { viewModel.closeCompleteSummaryDialog() },
      title = {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = PastelEmeraldBg,
            modifier = Modifier.size(40.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PastelEmeraldText, modifier = Modifier.size(24.dp))
            }
          }
          Column {
            Text("Job Completed Successfully!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
            Text(job.workOrderNumber, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
          }
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = SurfaceCardSubtle,
            border = BorderStroke(1.dp, DividerLight),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Text("Summary of Service:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextMuted))
              Text(job.issueTitle, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary))
              Text("Customer: ${job.customer.name}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
              Text("Completed at: ${job.completionTimestamp ?: "Just now"}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Total Invoice: $${String.format("%.2f", job.invoiceAmount)}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandIndigo)
              )
            }
          }

          Text(
            text = "Customer has been notified and service ticket is synched with dispatch.",
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.closeCompleteSummaryDialog()
            viewModel.selectTab(RepairTab.TODAY)
          },
          colors = ButtonDefaults.buttonColors(containerColor = BrandTealPrimary),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Return to Today's Jobs")
        }
      },
      dismissButton = {
        OutlinedButton(
          onClick = { viewModel.closeCompleteSummaryDialog() },
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Stay on Details")
        }
      },
      containerColor = SurfaceCard,
      shape = RoundedCornerShape(16.dp)
    )
  }
}
