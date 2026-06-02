import SwiftUI
import FlightRewardsFeature

@main
struct FlightRewardsApp: App {

    var body: some Scene {
        WindowGroup {
            FlightRewardsLoginView(
                viewModel: AuthViewModel(
                    authService: AuthService(),
                    networkMonitor: StaticNetworkMonitor(isOnline: true),
                    tokenStore: UserDefaultsTokenStore()
                )
            )
        }
    }
}
