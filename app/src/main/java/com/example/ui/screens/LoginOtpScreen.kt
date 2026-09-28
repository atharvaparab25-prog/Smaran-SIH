package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.UserRole
import com.example.ui.theme.SmaranGreenContainer
import com.example.ui.theme.SmaranGreenPrimary
import com.example.ui.theme.SmaranTextPrimary

private const val SHARE_APP_URL = "https://ais-pre-u425mxp4ah6wxgggm4bvje-594314281114.asia-southeast1.run.app"

@Composable
fun LoginOtpScreen(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onLoginSuccess: (UserRole) -> Unit,
    onRegisterSuccess: (name: String, role: UserRole, phone: String, language: AppLanguage) -> Unit = { _, role, _, _ -> onLoginSuccess(role) }
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Register

    // Login Form State
    var loginPhone by remember { mutableStateOf("+91 98640 12345") }
    var loginOtp by remember { mutableStateOf("1234") }
    var loginError by remember { mutableStateOf<String?>(null) }

    // Register Form State
    var regRole by remember { mutableStateOf(currentRole) }
    var regName by remember { mutableStateOf("Bhaben Baruah") }
    var regPhone by remember { mutableStateOf("+91 98640 12345") }
    var regEmergencyPhone by remember { mutableStateOf("+91 98640 54321") }
    var regSecondaryDetail by remember { mutableStateOf("Son (Living together)") }
    var regLocation by remember { mutableStateOf("Guwahati, Assam") }
    var regLanguage by remember { mutableStateOf(AppLanguage.ASSAMESE) }
    var regPin by remember { mutableStateOf("1234") }
    var regError by remember { mutableStateOf<String?>(null) }

    fun shareAppWithFriends() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out Smaran (স্মৰণ) - Dementia Memory Companion & Cognitive Health App:\n$SHARE_APP_URL"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Smaran with Friends")
        try {
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open share dialog", Toast.LENGTH_SHORT).show()
        }
    }

    fun copyShareLink() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Smaran App Link", SHARE_APP_URL)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Link copied to clipboard! Share with friends.", Toast.LENGTH_SHORT).show()
    }

    fun executeLogin() {
        if (loginPhone.isBlank()) {
            loginError = "Please enter your mobile phone number."
            return
        }
        if (loginOtp.length != 4) {
            loginError = "Please enter the 4-digit PIN / OTP (e.g. 1234)."
            return
        }
        loginError = null
        Toast.makeText(context, "Logging in as ${currentRole.title}...", Toast.LENGTH_SHORT).show()
        onLoginSuccess(currentRole)
    }

    fun executeRegister() {
        if (regName.isBlank()) {
            regError = "Please enter your name."
            return
        }
        if (regPhone.isBlank()) {
            regError = "Please enter your phone number."
            return
        }
        if (regPin.length != 4) {
            regError = "Please set a 4-digit security PIN."
            return
        }
        regError = null
        Toast.makeText(context, "Account created! Welcome to Smaran as ${regRole.title}.", Toast.LENGTH_SHORT).show()
        onRegisterSuccess(regName, regRole, regPhone, regLanguage)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("login_otp_screen"),
        // FIXED STICKY GREEN ACTION BUTTON AT THE BOTTOM
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 12.dp,
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    if (selectedTab == 0) {
                        Button(
                            onClick = { executeLogin() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("login_submit_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF15803D), // Vibrant Dark Emerald Green
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "ENTER AS ${currentRole.title.uppercase()} (প্ৰৱেশ কৰক) →",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        }
                    } else {
                        Button(
                            onClick = { executeRegister() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("btn_register_submit"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF15803D), // Vibrant Dark Emerald Green
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "REGISTER & ENTER AS ${regRole.title.uppercase()} ✓",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAF8))
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Banner
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFC8E6C9))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF15803D),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("🧠", fontSize = 22.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Smaran • স্মৰণ",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF15803D)
                                    )
                                    Text(
                                        text = "Dementia Memory Companion • অসম",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Share Quick Button
                            OutlinedButton(
                                onClick = { shareAppWithFriends() },
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("btn_share_header")
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF15803D), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Share", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                            }
                        }
                    }
                }
            }

            // Tab Switcher: Login vs Register
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.White,
                        contentColor = Color(0xFF15803D),
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = Color(0xFF15803D),
                                height = 3.dp
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    text = "🔑 1. Login / প্ৰৱেশ",
                                    fontWeight = if (selectedTab == 0) FontWeight.ExtraBold else FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = if (selectedTab == 0) Color(0xFF15803D) else Color.Gray
                                )
                            },
                            modifier = Modifier.testTag("tab_login")
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    text = "📝 2. Register / পঞ্জীয়ন",
                                    fontWeight = if (selectedTab == 1) FontWeight.ExtraBold else FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = if (selectedTab == 1) Color(0xFF15803D) else Color.Gray
                                )
                            },
                            modifier = Modifier.testTag("tab_register")
                        )
                    }
                }
            }

            // ==========================================
            // TAB 0: LOGIN MODE
            // ==========================================
            if (selectedTab == 0) {
                // Step 1: Select Active Role (SELECT ONLY - DOES NOT NAVIGATE!)
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "1. Select Role to Login",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SmaranTextPrimary
                                )
                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Active: ${currentRole.title}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            val roles = listOf(
                                Triple(UserRole.PATIENT, "👴 Patient / Elder", "Bhaben Baruah (74) • Guwahati, Assam"),
                                Triple(UserRole.CAREGIVER, "👨‍💼 Family Caregiver", "Rahul Baruah (Son) • Remote Care"),
                                Triple(UserRole.ASHA_WORKER, "👩‍⚕️ ASHA Health Worker", "Anjali Deka • Sub-Center 04")
                            )

                            roles.forEach { (role, roleLabel, roleDesc) ->
                                val isSelected = currentRole == role

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            // SELECT ROLE ONLY - DOES NOT JUMP TO DASHBOARD
                                            onRoleSelected(role)
                                        }
                                        .testTag("select_role_${role.name}"),
                                    color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF9FAFB),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) Color(0xFF15803D) else Color(0xFFE5E7EB)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = roleLabel,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color(0xFF15803D) else SmaranTextPrimary
                                            )
                                            Text(
                                                text = roleDesc,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF4B5563)
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Selected",
                                                tint = Color(0xFF15803D),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Step 2: Enter Credentials
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "2. Enter Credentials for ${currentRole.title}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SmaranTextPrimary
                            )

                            OutlinedTextField(
                                value = loginPhone,
                                onValueChange = { loginPhone = it },
                                label = { Text(when (currentRole) {
                                    UserRole.PATIENT -> "Registered Patient Mobile"
                                    UserRole.CAREGIVER -> "Registered Caregiver Mobile"
                                    UserRole.ASHA_WORKER -> "ASHA Mobile / Worker ID"
                                }) },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF15803D))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_phone_number"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = loginOtp,
                                onValueChange = { if (it.length <= 4) loginOtp = it },
                                label = { Text("4-Digit Security PIN / OTP") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF15803D))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_otp"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        loginOtp = "1234"
                                        loginPhone = when (currentRole) {
                                            UserRole.PATIENT -> "+91 98640 12345"
                                            UserRole.CAREGIVER -> "+91 98640 54321"
                                            UserRole.ASHA_WORKER -> "+91 94350 67890"
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("✨ Auto-fill Demo (1234)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        loginPhone = ""
                                        loginOtp = ""
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Clear", fontSize = 12.sp)
                                }
                            }

                            if (loginError != null) {
                                Text(
                                    text = loginError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Switch to Register prompt
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Don't have an account? ", color = Color(0xFF6B7280), fontSize = 13.sp)
                        Text(
                            text = "Register Here / পঞ্জীয়ন কৰক",
                            color = Color(0xFF15803D),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .clickable { selectedTab = 1 }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // ==========================================
            // TAB 1: REGISTER MODE
            // ==========================================
            if (selectedTab == 1) {
                // Role Selector for Registration
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "1. Choose Account Role to Register",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SmaranTextPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    Pair(UserRole.PATIENT, "👴 Patient"),
                                    Pair(UserRole.CAREGIVER, "👨‍💼 Caregiver"),
                                    Pair(UserRole.ASHA_WORKER, "👩‍⚕️ ASHA")
                                ).forEach { (r, label) ->
                                    val isSelected = regRole == r
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable {
                                                regRole = r
                                                onRoleSelected(r)
                                                // Adjust initial field suggestions based on role
                                                when (r) {
                                                    UserRole.PATIENT -> {
                                                        regName = "Bhaben Baruah"
                                                        regPhone = "+91 98640 12345"
                                                        regSecondaryDetail = "Guwahati, Assam"
                                                    }
                                                    UserRole.CAREGIVER -> {
                                                        regName = "Rahul Baruah"
                                                        regPhone = "+91 98640 54321"
                                                        regSecondaryDetail = "Son (Caring for Bhaben Baruah)"
                                                    }
                                                    UserRole.ASHA_WORKER -> {
                                                        regName = "Anjali Deka"
                                                        regPhone = "+91 94350 67890"
                                                        regSecondaryDetail = "Sub-Center 04, Guwahati PHC"
                                                    }
                                                }
                                            },
                                        color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF3F4F6),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.5.dp,
                                            if (isSelected) Color(0xFF15803D) else Color.Transparent
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color(0xFF15803D) else Color(0xFF374151)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Registration Fields Form
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "2. Details for ${regRole.title}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SmaranTextPrimary
                            )

                            // Full Name
                            OutlinedTextField(
                                value = regName,
                                onValueChange = { regName = it },
                                label = { Text(when (regRole) {
                                    UserRole.PATIENT -> "Patient's Full Name (নাম)"
                                    UserRole.CAREGIVER -> "Caregiver's Full Name"
                                    UserRole.ASHA_WORKER -> "ASHA Worker's Full Name"
                                }) },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF15803D))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_reg_name"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            // Phone
                            OutlinedTextField(
                                value = regPhone,
                                onValueChange = { regPhone = it },
                                label = { Text("Mobile Phone Number") },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF15803D))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_reg_phone"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            // Role Specific Secondary Detail
                            OutlinedTextField(
                                value = regSecondaryDetail,
                                onValueChange = { regSecondaryDetail = it },
                                label = { Text(when (regRole) {
                                    UserRole.PATIENT -> "Home City / Location"
                                    UserRole.CAREGIVER -> "Relationship to Patient (e.g. Son, Daughter)"
                                    UserRole.ASHA_WORKER -> "Assigned Sub-Center / PHC"
                                }) },
                                leadingIcon = {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF15803D))
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            // Emergency Contact (for SOS)
                            OutlinedTextField(
                                value = regEmergencyPhone,
                                onValueChange = { regEmergencyPhone = it },
                                label = { Text("Emergency Family Contact (for SOS)") },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFFC62828))
                                },
                                supportingText = {
                                    Text("This number receives GPS alert during panic / fall events", fontSize = 11.sp)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_reg_emergency_phone"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            // Preferred Language
                            Text(
                                text = "Preferred Regional Language:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF374151)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(
                                    Pair(AppLanguage.ASSAMESE, "অসমীয়া"),
                                    Pair(AppLanguage.ENGLISH, "English"),
                                    Pair(AppLanguage.HINDI, "हिन्दी"),
                                    Pair(AppLanguage.BENGALI, "বাংলা")
                                ).forEach { (lang, label) ->
                                    val isSelected = regLanguage == lang
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { regLanguage = lang },
                                        color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF3F4F6),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) Color(0xFF15803D) else Color.Transparent
                                        )
                                    ) {
                                        Box(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color(0xFF15803D) else Color(0xFF374151)
                                            )
                                        }
                                    }
                                }
                            }

                            // 4-Digit Security PIN
                            OutlinedTextField(
                                value = regPin,
                                onValueChange = { if (it.length <= 4) regPin = it },
                                label = { Text("Create 4-Digit Security PIN") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF15803D))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_reg_pin"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            if (regError != null) {
                                Text(
                                    text = regError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Switch to Login prompt
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Already registered? ", color = Color(0xFF6B7280), fontSize = 13.sp)
                        Text(
                            text = "Sign In Here / প্ৰৱেশ কৰক",
                            color = Color(0xFF15803D),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .clickable { selectedTab = 0 }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Share Smaran Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFBFDBFE)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_share_friends")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFDBEAFE),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🤝", fontSize = 18.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Share Smaran with Friends",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Text(
                                    text = "Send link to friends, family or doctors",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF3B82F6)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { shareAppWithFriends() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Share Link", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { copyShareLink() },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy Link", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF2563EB))
                            }
                        }
                    }
                }
            }

            // Extra space so list items are not obscured by the bottom bar
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
