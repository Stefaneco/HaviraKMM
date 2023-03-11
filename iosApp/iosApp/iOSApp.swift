import SwiftUI
import shared

@main
struct iOSApp: App {
    
    private var appModule: any IAppModule = AppModule()
    
	var body: some Scene {
		WindowGroup {
            NavigationView {
                ContentView(appModule: appModule)
            }
		}
	}
}
