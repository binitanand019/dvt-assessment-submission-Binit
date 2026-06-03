import Foundation
import Security

public actor KeychainTokenStore: TokenStoring {

    private let service: String
    private let account: String

    public init(
        service: String = "FlightRewards",
        account: String = "authToken"
    ) {
        self.service = service
        self.account = account
    }

    public func saveToken(_ token: String) async {

        guard let data = token.data(using: .utf8) else {
            return
        }

        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account
        ]

        SecItemDelete(query as CFDictionary)

        let attributes: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account,
            kSecValueData as String: data
        ]

        SecItemAdd(attributes as CFDictionary, nil)
    }

    public func clearToken() async {

        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account
        ]

        SecItemDelete(query as CFDictionary)
    }

    public func readToken() async -> String? {

        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: service,
            kSecAttrAccount as String: account,
            kSecReturnData as String: true,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]

        var item: CFTypeRef?

        let status = SecItemCopyMatching(
            query as CFDictionary,
            &item
        )

        guard
            status == errSecSuccess,
            let data = item as? Data,
            let token = String(
                data: data,
                encoding: .utf8
            )
        else {
            return nil
        }

        return token
    }
}
