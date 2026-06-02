package com.binit.flightrewards.ui.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.binit.flightrewards.viewmodel.LoginEvent
import com.binit.flightrewards.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
  viewModel: LoginViewModel = hiltViewModel(),
  onLoginSuccess: () -> Unit
) {

  val state by viewModel.uiState.collectAsState()

  val focusManager = LocalFocusManager.current

  val snackbarHostState = remember {
    SnackbarHostState()
  }

  LaunchedEffect(Unit) {

    viewModel.events.collect { event ->

      when (event) {

        LoginEvent.NavigateToHome -> {
          onLoginSuccess()
        }
      }
    }
  }

  LaunchedEffect(state.errorMessage) {

    state.errorMessage?.let {

      snackbarHostState.showSnackbar(it)
    }
  }

  Surface(
    modifier = Modifier.fillMaxSize()
  ) {

    Box(
      modifier = Modifier.fillMaxSize()
    ) {

      Column(
        modifier = Modifier
          .fillMaxSize()
          .statusBarsPadding()
          .padding(24.dp),
        verticalArrangement = Arrangement.Center
      ) {

        Text(
          text = "Flight Rewards",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Sign in to continue",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("email")
            .semantics {
              contentDescription =
                "Email address input field"
            },
          value = state.email,
          onValueChange = viewModel::onEmailChanged,
          label = {
            Text("Email")
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
          ),
          keyboardActions = KeyboardActions(
            onNext = {
              focusManager.moveFocus(
                FocusDirection.Down
              )
            }
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("password")
            .semantics {
              contentDescription =
                "Password input field"
            },
          value = state.password,
          onValueChange = viewModel::onPasswordChanged,
          label = {
            Text("Password")
          },
          singleLine = true,
          visualTransformation =
            PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = {
              focusManager.clearFocus()

              if (state.isLoginEnabled) {
                viewModel.onLoginClicked()
              }
            }
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {

          Checkbox(
            checked = state.rememberMe,
            onCheckedChange =
              viewModel::onRememberMeChanged,
            modifier = Modifier
              .testTag("rememberMe")
          )

          Text(
            text = "Remember me",
            modifier = Modifier.clickable {
              viewModel.onRememberMeChanged(
                !state.rememberMe
              )
            }
          )

          Spacer(modifier = Modifier.weight(1f))

          Text(
            text = if (state.isOnline) {
              "Online"
            } else {
              "Offline"
            },
            color =
              if (state.isOnline) {
                MaterialTheme.colorScheme.primary
              } else {
                MaterialTheme.colorScheme.error
              },
            modifier = Modifier.testTag(
              "networkStatus"
            )
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("loginButton"),
          onClick = {
            viewModel.onLoginClicked()
          },
          enabled = state.isLoginEnabled
        ) {

          if (state.isLoading) {

            CircularProgressIndicator()

          } else {

            Text(
              text = "Login"
            )
          }
        }
      }

      SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(16.dp)
      )
    }
  }
}