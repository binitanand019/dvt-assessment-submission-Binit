import SwiftUI

public struct FlightRewardsLoginView: View {

    @StateObject private var viewModel: AuthViewModel

    public init(viewModel: AuthViewModel) {
        _viewModel = StateObject(wrappedValue: viewModel)
    }

    public var body: some View {

        Group {
            if viewModel.isLoggedIn {
                HomeView(
                    email: viewModel.state.email,
                    onLogout: {
                        Task {
                            await viewModel.logoutTapped()
                        }
                    }
                )
            } else {
                loginContent
            }
        }
        .animation(.easeInOut(duration: 0.2), value: viewModel.isLoggedIn)
        .task {
            await viewModel.checkStoredToken()
        }
    }

    private var loginContent: some View {

        ZStack {

            LinearGradient(
                colors: [
                    Color.blue.opacity(0.12),
                    Color.white
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()

            VStack {

                Spacer()

                VStack(alignment: .leading, spacing: 20) {

                    VStack(alignment: .leading, spacing: 6) {

                        Text("Flight Rewards")
                            .font(.largeTitle)
                            .fontWeight(.bold)

                        Text("Sign in to continue")
                            .foregroundStyle(.secondary)
                    }

                    VStack(spacing: 16) {

                        TextField(
                            "Email address",
                            text: Binding(
                                get: { viewModel.state.email },
                                set: { viewModel.onEmailChanged($0) }
                            )
                        )
                        .padding()
                        .background(Color.white)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                        .overlay(
                            RoundedRectangle(cornerRadius: 14)
                                .stroke(emailBorderColor, lineWidth: 1)
                        )
                        #if os(iOS)
                        .textInputAutocapitalization(.never)
                        .keyboardType(.emailAddress)
                        .autocorrectionDisabled(true)
                        #endif
                        .accessibilityIdentifier("emailField")

                        SecureField(
                            "Password",
                            text: Binding(
                                get: { viewModel.state.password },
                                set: { viewModel.onPasswordChanged($0) }
                            )
                        )
                        .padding()
                        .background(Color.white)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                        .overlay(
                            RoundedRectangle(cornerRadius: 14)
                                .stroke(passwordBorderColor, lineWidth: 1)
                        )
                        .accessibilityIdentifier("passwordField")
                    }

                    Toggle(
                        "Remember me",
                        isOn: Binding(
                            get: { viewModel.state.rememberMe },
                            set: { viewModel.onRememberMeChanged($0) }
                        )
                    )
                    .accessibilityIdentifier("rememberMeToggle")

                    if let message = viewModel.state.errorMessage {

                        HStack(spacing: 8) {

                            Image(systemName: "exclamationmark.triangle.fill")
                                .foregroundStyle(.red)

                            Text(message)
                                .font(.subheadline)
                                .foregroundStyle(.red)
                        }
                    }

                    Button {

                        Task {
                            await viewModel.loginTapped()
                        }

                    } label: {

                        HStack {

                            Spacer()

                            if viewModel.state.isLoading {
                                ProgressView()
                                    .controlSize(.small)
                                    .tint(.white)
                            } else {
                                Text("Login")
                                    .fontWeight(.semibold)
                            }

                            Spacer()
                        }
                        .padding()
                        .background(
                            viewModel.state.isLoginEnabled
                            ? Color.blue
                            : Color.gray.opacity(0.5)
                        )
                        .foregroundStyle(.white)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                    }
                    .disabled(
                        !viewModel.state.isLoginEnabled ||
                        viewModel.state.isLoading
                    )
                    .accessibilityIdentifier("loginButton")

                    VStack(alignment: .leading, spacing: 6) {

                        Text("Demo Credentials")
                            .font(.caption)
                            .fontWeight(.semibold)
                            .foregroundStyle(.secondary)

                        Text("Email: test@example.com")
                            .font(.caption2)
                            .foregroundStyle(.secondary)

                        Text("Password: Password1")
                            .font(.caption2)
                            .foregroundStyle(.secondary)
                    }

                }
                .padding(24)
                .background(Color.white.opacity(0.95))
                .clipShape(RoundedRectangle(cornerRadius: 24))
                .shadow(color: .black.opacity(0.08), radius: 16)

                Spacer()
            }
            .padding()
        }
    }

    private var emailBorderColor: Color {

        if viewModel.state.email.isEmpty {
            return .gray.opacity(0.4)
        }

        return viewModel.state.isValidEmail
        ? .green
        : .red
    }

    private var passwordBorderColor: Color {

        if viewModel.state.password.isEmpty {
            return .gray.opacity(0.4)
        }

        return viewModel.state.isValidPassword
        ? .green
        : .red
    }
}

private struct HomeView: View {

    let email: String
    let onLogout: () -> Void

    var body: some View {

        NavigationStack {

            VStack(spacing: 24) {

                Spacer()

                Image(systemName: "checkmark.circle.fill")
                    .font(.system(size: 72))
                    .foregroundStyle(.green)

                Text("Login Successful")
                    .font(.largeTitle)
                    .fontWeight(.bold)

                Text("Welcome back, \(email)")
                    .foregroundStyle(.secondary)

                Button(action: onLogout) {

                    Text("Logout")
                        .fontWeight(.semibold)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.red)
                        .foregroundStyle(.white)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                }
                .padding(.top, 12)

                Spacer()
            }
            .padding(24)
            .navigationTitle("Home")
        }
    }
}
