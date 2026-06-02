package com.binit.flightrewards

import app.cash.turbine.test
import com.binit.flightrewards.data.repository.AuthException
import com.binit.flightrewards.data.repository.AuthRepository
import com.binit.flightrewards.data.storage.TokenStore
import com.binit.flightrewards.viewmodel.LoginEvent
import com.binit.flightrewards.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

  private val dispatcher = StandardTestDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun validation_enablesDisablesButton() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    assertFalse(vm.uiState.value.isLoginEnabled)

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("12345")

    assertFalse(vm.uiState.value.isLoginEnabled)

    vm.onPasswordChanged("123456")

    assertTrue(vm.uiState.value.isLoginEnabled)
  }

  @Test
  fun success_emitsNavigationEvent() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } returns "token-xyz"

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")

    vm.events.test {

      vm.onLoginClicked()

      dispatcher.scheduler.advanceUntilIdle()

      assertEquals(
        LoginEvent.NavigateToHome,
        awaitItem()
      )

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun error_incrementsFailureCount() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } throws AuthException("no")

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")

    vm.onLoginClicked()

    dispatcher.scheduler.advanceUntilIdle()

    assertEquals(
      1,
      vm.uiState.value.failureCount
    )

    assertFalse(vm.uiState.value.isLockedOut)
  }

  @Test
  fun lockout_afterThreeFailures() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } throws AuthException("no")

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")

    repeat(3) {

      vm.onLoginClicked()

      dispatcher.scheduler.advanceUntilIdle()
    }

    assertEquals(
      3,
      vm.uiState.value.failureCount
    )

    assertTrue(vm.uiState.value.isLockedOut)

    assertNotNull(
      vm.uiState.value.lockoutExpiryTime
    )

    assertEquals(
      "Too many failed attempts. Try again in 5 minutes.",
      vm.uiState.value.errorMessage
    )
  }

  @Test
  fun offline_showsMessageAndDoesNotCallService() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = false)

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    dispatcher.scheduler.advanceUntilIdle()

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")

    vm.onLoginClicked()

    dispatcher.scheduler.advanceUntilIdle()

    assertEquals(
      "You appear to be offline",
      vm.uiState.value.errorMessage
    )

    coVerify(exactly = 0) {
      repo.login(any(), any())
    }
  }

  @Test
  fun rememberMe_persistsToken() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } returns "token-xyz"

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")
    vm.onRememberMeChanged(true)

    vm.onLoginClicked()

    dispatcher.scheduler.advanceUntilIdle()

    coVerify(exactly = 1) {
      tokenStore.saveToken("token-xyz")
    }
  }

  @Test
  fun emailValidation_rejectsInvalidFormat() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("invalid-email")

    vm.onPasswordChanged("123456")

    assertFalse(
      vm.uiState.value.isLoginEnabled
    )
  }

  @Test
  fun rememberMeUnchecked_clearsTokenOnLogin() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } returns "token-xyz"

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")
    vm.onRememberMeChanged(false)

    vm.onLoginClicked()

    dispatcher.scheduler.advanceUntilIdle()

    coVerify(exactly = 1) {
      tokenStore.clearToken()
    }

    coVerify(exactly = 0) {
      tokenStore.saveToken(any())
    }
  }

  @Test
  fun networkStatusUpdates_reflectedInUiState() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    dispatcher.scheduler.advanceUntilIdle()

    assertTrue(vm.uiState.value.isOnline)

    network.setOnline(false)

    dispatcher.scheduler.advanceUntilIdle()

    assertFalse(vm.uiState.value.isOnline)
  }

  @Test
  fun specificErrorMessages_forDifferentExceptions() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } throws AuthException("Account disabled")

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")

    vm.onLoginClicked()

    dispatcher.scheduler.advanceUntilIdle()

    assertEquals(
      "Account disabled",
      vm.uiState.value.errorMessage
    )
  }

  @Test
  fun logout_clearsTokenAndResetsState() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } returns "token-xyz"

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")
    vm.onRememberMeChanged(true)

    vm.onLoginClicked()

    dispatcher.scheduler.advanceUntilIdle()

    assertTrue(vm.isLoggedIn.value)

    vm.logout()

    dispatcher.scheduler.advanceUntilIdle()

    assertFalse(vm.isLoggedIn.value)

    assertEquals(
      "",
      vm.uiState.value.email
    )

    assertEquals(
      "",
      vm.uiState.value.password
    )

    coVerify {
      tokenStore.clearToken()
    }
  }

  @Test
  fun loadingState_updatesCorrectly() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } returns "token-xyz"

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")

    vm.onLoginClicked()

    assertTrue(vm.uiState.value.isLoading)

    advanceUntilIdle()

    assertFalse(vm.uiState.value.isLoading)
  }

  @Test
  fun existingToken_autoLogsInUser() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>()

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      tokenStore.readToken()
    } returns "existing-token"

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    advanceUntilIdle()

    assertTrue(vm.isLoggedIn.value)
  }

  @Test
  fun timeoutException_showsCorrectMessage() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } throws SocketTimeoutException()

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")

    vm.onLoginClicked()

    advanceUntilIdle()

    assertEquals(
      "Request timed out. Please try again.",
      vm.uiState.value.errorMessage
    )
  }

  @Test
  fun unknownHostException_showsCorrectMessage() = runTest {

    val repo = mockk<AuthRepository>()

    val tokenStore =
      mockk<TokenStore>(relaxed = true)

    val network =
      InMemoryNetworkMonitor(isOnline = true)

    coEvery {
      repo.login(any(), any())
    } throws UnknownHostException()

    val vm = LoginViewModel(
      repo,
      network,
      tokenStore
    )

    vm.onEmailChanged("a@b.com")
    vm.onPasswordChanged("123456")

    vm.onLoginClicked()

    advanceUntilIdle()

    assertEquals(
      "Unable to reach server. Please check your connection.",
      vm.uiState.value.errorMessage
    )
  }
}