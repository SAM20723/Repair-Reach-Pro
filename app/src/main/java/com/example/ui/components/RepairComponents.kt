package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Customer
import com.example.data.model.JobItem
import com.example.data.model.JobPriority
import com.example.data.model.JobStatus
import com.example.ui.theme.ActionTeal
import com.example.ui.theme.ActionTealLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandIndigo
import com.example.ui.theme.BrandTealPrimary
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
import com.example.ui.theme.PastelPurpleBg
import com.example.ui.theme.PastelPurpleBorder
import com.example.ui.theme.PastelPurpleText
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

@Composable
fun JobStatusBadge(
  status: JobStatus,
  modifier: Modifier = Modifier
) {
  val (bgColor, borderColor, textColor, icon) = when (status) {
    JobStatus.PENDING -> Quad(PastelAmberBg, PastelAmberBorder, PastelAmberText, Icons.Default.Schedule)
    JobStatus.EN_ROUTE -> Quad(PastelSkyBg, PastelSkyBorder, PastelSkyText, Icons.Default.DirectionsCar)
    JobStatus.IN_PROGRESS -> Quad(PastelIndigoBg, PastelIndigoBorder, PastelIndigoText, Icons.Default.Build)
    JobStatus.COMPLETED -> Quad(PastelEmeraldBg, PastelEmeraldBorder, PastelEmeraldText, Icons.Default.CheckCircle)
  }

  Surface(
    modifier = modifier.testTag("status_badge_${status.name.lowercase()}"),
    shape = RoundedCornerShape(20.dp),
    color = bgColor,
    border = BorderStroke(1.dp, borderColor)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = textColor,
        modifier = Modifier.size(13.dp)
      )
      Text(
        text = status.displayName,
        color = textColor,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = 12.sp
        )
      )
    }
  }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun PriorityBadge(priority: JobPriority) {
  when (priority) {
    JobPriority.EMERGENCY -> {
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = PastelRoseBg,
        border = BorderStroke(1.dp, PastelRoseBorder)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
          Icon(Icons.Default.PriorityHigh, contentDescription = null, tint = PastelRoseText, modifier = Modifier.size(11.dp))
          Text("EMERGENCY", color = PastelRoseText, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp))
        }
      }
    }
    JobPriority.HIGH -> {
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = PastelAmberBg,
        border = BorderStroke(1.dp, PastelAmberBorder)
      ) {
        Text(
          text = "HIGH PRIORITY",
          color = PastelAmberText,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)
        )
      }
    }
    else -> {}
  }
}

@Composable
fun MetricSummaryCard(
  totalJobs: Int,
  completedJobs: Int,
  capacityBooked: Double,
  capacityTotal: Double = 8.0,
  onTimePercent: Int = 98,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("metric_summary_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = BorderStroke(1.dp, DividerLight),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(18.dp)
    ) {
      // Top row: Header badge and date
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(ActionTeal)
          )
          Text(
            text = "TODAY'S DISPATCH METRICS",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              color = TextMuted
            )
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = PastelEmeraldBg,
          border = BorderStroke(1.dp, PastelEmeraldBorder)
        ) {
          Text(
            text = "$onTimePercent% On-Time",
            color = PastelEmeraldText,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 3 Stat Columns: Jobs Today | Completed | Weekly Capacity
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Stat 1: Jobs Assigned
        Surface(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(12.dp),
          color = PastelPurpleBg,
          border = BorderStroke(1.dp, PastelPurpleBorder)
        ) {
          Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "$totalJobs",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
            Text(
              text = "Jobs Today",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }

        // Stat 2: Completed
        Surface(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(12.dp),
          color = PastelIndigoBg,
          border = BorderStroke(1.dp, PastelIndigoBorder)
        ) {
          Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "$completedJobs",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )
              Text(
                text = "/$totalJobs",
                style = MaterialTheme.typography.titleSmall.copy(
                  color = TextSecondary,
                  fontWeight = FontWeight.Medium
                )
              )
            }
            Text(
              text = "Completed",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }

        // Stat 3: Capacity
        Surface(
          modifier = Modifier.weight(1.1f),
          shape = RoundedCornerShape(12.dp),
          color = PastelSkyBg,
          border = BorderStroke(1.dp, PastelSkyBorder)
        ) {
          Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "32h",
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
              )
            )
            Text(
              text = "Weekly Cap.",
              style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Linear Shift Capacity Bar
      val progress = (completedJobs.toFloat() / totalJobs.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(SurfaceCardSubtle)
          .padding(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Shift Progress: ${(progress * 100).toInt()}% Done",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.SemiBold,
              color = TextPrimary
            )
          )
          Text(
            text = "${capacityBooked}h scheduled of ${capacityTotal}h shift",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(7.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = ActionTeal,
          trackColor = DividerLight
        )
      }
    }
  }
}

@Composable
fun JobCard(
  job: JobItem,
  onClick: () -> Unit,
  onNavigateClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("job_card_${job.id}")
      .clickable { onClick() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = BorderStroke(
      width = if (job.status == JobStatus.IN_PROGRESS || job.status == JobStatus.EN_ROUTE) 1.5.dp else 1.dp,
      color = if (job.status == JobStatus.IN_PROGRESS) BrandTealPrimary
      else if (job.status == JobStatus.EN_ROUTE) ActionTeal
      else DividerLight
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      // Top row: Scheduled time + Dynamic Status Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = ActionTeal,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "${job.scheduledTimeStart} – ${job.scheduledTimeEnd}",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
        }

        JobStatusBadge(status = job.status)
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Customer Name & Priority
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = job.customer.name,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        PriorityBadge(priority = job.priority)
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Problem description
      Text(
        text = job.issueTitle,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.SemiBold,
          color = TextSecondary
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Text(
        text = job.issueDescription,
        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 2.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Equipment & Address Badges
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = SurfaceCardSubtle,
          border = BorderStroke(1.dp, DividerLight),
          modifier = Modifier.weight(1f, fill = false)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Build, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(12.dp))
            Text(
              text = job.equipment.brand + " • " + job.equipment.modelNumber,
              style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Location & Quick Action Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(15.dp)
          )
          Text(
            text = "${job.customer.address} • ${job.distanceMiles} mi",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextSecondary,
              fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          // Action button
          Surface(
            modifier = Modifier
              .clickable { onClick() }
              .testTag("view_job_${job.id}"),
            shape = RoundedCornerShape(8.dp),
            color = if (job.status == JobStatus.IN_PROGRESS || job.status == JobStatus.EN_ROUTE) BrandTealPrimary else SurfaceCardSubtle,
            border = BorderStroke(1.dp, if (job.status == JobStatus.IN_PROGRESS) BrandTealPrimary else DividerLight)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = if (job.status == JobStatus.IN_PROGRESS) "Resume Job" else if (job.status == JobStatus.EN_ROUTE) "View Route" else "View Details",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (job.status == JobStatus.IN_PROGRESS || job.status == JobStatus.EN_ROUTE) Color.White else TextPrimary
                )
              )
              Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = if (job.status == JobStatus.IN_PROGRESS || job.status == JobStatus.EN_ROUTE) Color.White else TextPrimary,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun MapDestinationView(
  job: JobItem,
  onNavigateClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .height(190.dp)
      .testTag("map_destination_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
    border = BorderStroke(1.dp, DividerLight)
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      // Vector Graphic Map Canvas representation
      Canvas(
        modifier = Modifier
          .fillMaxWidth()
          .height(190.dp)
      ) {
        val w = size.width
        val h = size.height

        // Background terrain / subtle zone grid
        drawRect(color = Color(0xFFE2E8F0))

        // Street grid lines
        val roadPaint = Color(0xFFCBD5E1)
        val roadMajor = Color(0xFFFFFFFF)

        // Horizontal roads
        drawLine(color = roadPaint, start = Offset(0f, h * 0.25f), end = Offset(w, h * 0.25f), strokeWidth = 8f)
        drawLine(color = roadMajor, start = Offset(0f, h * 0.55f), end = Offset(w, h * 0.55f), strokeWidth = 14f)
        drawLine(color = roadPaint, start = Offset(0f, h * 0.85f), end = Offset(w, h * 0.85f), strokeWidth = 8f)

        // Vertical roads
        drawLine(color = roadPaint, start = Offset(w * 0.2f, 0f), end = Offset(w * 0.2f, h), strokeWidth = 8f)
        drawLine(color = roadMajor, start = Offset(w * 0.6f, 0f), end = Offset(w * 0.6f, h), strokeWidth = 16f)
        drawLine(color = roadPaint, start = Offset(w * 0.85f, 0f), end = Offset(w * 0.85f, h), strokeWidth = 8f)

        // Diagonal Highway Route
        val routePath = Path().apply {
          moveTo(w * 0.15f, h * 0.85f)
          cubicTo(w * 0.35f, h * 0.75f, w * 0.45f, h * 0.58f, w * 0.6f, h * 0.55f)
          lineTo(w * 0.6f, h * 0.35f)
          lineTo(w * 0.75f, h * 0.35f)
        }

        // Route Shadow & Glow
        drawPath(
          path = routePath,
          color = Color(0x330D9488),
          style = Stroke(width = 14f, cap = StrokeCap.Round)
        )
        // Main Route line
        drawPath(
          path = routePath,
          color = ActionTeal,
          style = Stroke(
            width = 7f,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
          )
        )

        // Technician Current Position (Vehicle Dot)
        drawCircle(
          color = Color.White,
          radius = 11f,
          center = Offset(w * 0.15f, h * 0.85f)
        )
        drawCircle(
          color = BrandIndigo,
          radius = 7f,
          center = Offset(w * 0.15f, h * 0.85f)
        )

        // Destination Pin
        drawCircle(
          color = Color(0xFFDC2626),
          radius = 12f,
          center = Offset(w * 0.75f, h * 0.35f)
        )
        drawCircle(
          color = Color.White,
          radius = 5f,
          center = Offset(w * 0.75f, h * 0.35f)
        )
      }

      // Overlaid Destination Details Header
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // ETA Pill
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.95f),
            border = BorderStroke(1.dp, DividerLight),
            shadowElevation = 3.dp
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(Icons.Default.Navigation, contentDescription = null, tint = ActionTeal, modifier = Modifier.size(13.dp))
              Text(
                text = "${job.travelTimeMinutes} min (${job.distanceMiles} mi)",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary
                )
              )
            }
          }

          // Open GPS Turn-by-Turn Button
          Button(
            onClick = onNavigateClick,
            modifier = Modifier
              .height(36.dp)
              .testTag("btn_start_gps_nav"),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 0.dp)
          ) {
            Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Start GPS",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
            )
          }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Destination Address Banner
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Color.White.copy(alpha = 0.92f),
          border = BorderStroke(1.dp, DividerLight),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
            Column {
              Text(
                text = job.customer.address + if (job.customer.unitOrSuite.isNotEmpty()) ", ${job.customer.unitOrSuite}" else "",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              if (job.customer.gateCode.isNotEmpty()) {
                Text(
                  text = "Gate: ${job.customer.gateCode}",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun CustomerActionRow(
  customer: Customer,
  onCallClick: () -> Unit,
  onMessageClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    border = BorderStroke(1.dp, DividerLight)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = customer.name,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimary
            )
          )
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = PastelSkyBg,
            border = BorderStroke(1.dp, PastelSkyBorder)
          ) {
            Text(
              text = customer.propertyType,
              style = MaterialTheme.typography.labelSmall.copy(
                color = PastelSkyText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp
              ),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = customer.phone,
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextSecondary,
            fontWeight = FontWeight.Medium
          )
        )
        if (customer.accessNotes.isNotEmpty()) {
          Text(
            text = "Note: ${customer.accessNotes}",
            style = MaterialTheme.typography.labelSmall.copy(
              color = TextMuted,
              fontSize = 11.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Circular Call and Message buttons
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Message Button
        Surface(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .clickable { onMessageClick() }
            .testTag("btn_quick_message"),
          shape = CircleShape,
          color = PastelIndigoBg,
          border = BorderStroke(1.dp, PastelIndigoBorder)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Message,
              contentDescription = "Message Customer",
              tint = PastelIndigoText,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        // Call Button
        Surface(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .clickable { onCallClick() }
            .testTag("btn_quick_call"),
          shape = CircleShape,
          color = PastelEmeraldBg,
          border = BorderStroke(1.dp, PastelEmeraldBorder)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Call,
              contentDescription = "Call Customer",
              tint = PastelEmeraldText,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun StickyLifecycleActionBar(
  status: JobStatus,
  onActionClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val (btnText, btnIcon, btnColor, subtitle) = when (status) {
    JobStatus.PENDING -> Quad(
      "Start Travel",
      Icons.Default.Navigation,
      BrandTealPrimary,
      "Tap to begin route navigation and alert customer"
    )
    JobStatus.EN_ROUTE -> Quad(
      "Mark Arrived",
      Icons.Default.LocationOn,
      BrandIndigo,
      "Arrived at customer location • Start work order timer"
    )
    JobStatus.IN_PROGRESS -> Quad(
      "Complete Job",
      Icons.Default.CheckCircle,
      Color(0xFF059669),
      "Wrap up work order, capture photos, and finalize invoice"
    )
    JobStatus.COMPLETED -> Quad(
      "Job Completed ✓",
      Icons.Default.Verified,
      Color(0xFF475569),
      "Service signed off & customer receipt sent"
    )
  }

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .shadow(12.dp),
    color = SurfaceCard,
    border = BorderStroke(1.dp, DividerLight)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextMuted,
          fontSize = 11.sp
        ),
        modifier = Modifier.padding(bottom = 6.dp)
      )

      Button(
        onClick = onActionClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("sticky_lifecycle_btn"),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = btnColor),
        enabled = status != JobStatus.COMPLETED
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = btnIcon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = btnText,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = Color.White
            )
          )
        }
      }
    }
  }
}
