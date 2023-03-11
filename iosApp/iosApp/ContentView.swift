import SwiftUI
import shared

struct ContentView: View {
	
    let appModule : IAppModule

	var body: some View {
        ZStack {
            Color.background
                .ignoresSafeArea()
            CreateDishScreen(dishInteractors: appModule.dishInteractors)
        }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView(appModule: AppModule())
	}
}
