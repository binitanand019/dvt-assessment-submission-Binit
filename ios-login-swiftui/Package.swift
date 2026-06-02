// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "FlightRewardsFeature",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(
            name: "FlightRewardsFeature",
            targets: ["FlightRewardsFeature"]
        )
    ],
    targets: [
        .target(
            name: "FlightRewardsFeature",
            path: "Sources/FlightRewardsFeature",
            resources: [
                .process("Resources")
            ]
        ),
        .testTarget(
            name: "FlightRewardsFeatureTests",
            dependencies: ["FlightRewardsFeature"],
            path: "Tests/FlightRewardsFeatureTests"
        )
    ]
)
