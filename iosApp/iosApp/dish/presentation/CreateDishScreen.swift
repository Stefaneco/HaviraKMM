//
//  CreateDishScreen.swift
//  iosApp
//
//  Created by Piotr Kalin on 02/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct CreateDishScreen: View {
    
    private var dishInteractors : DishInteractors
    @ObservedObject var viewModel : IOSCreateDishViewModel
    
    init(dishInteractors: DishInteractors) {
        self.dishInteractors = dishInteractors
        self.viewModel = IOSCreateDishViewModel(dishInteractors: dishInteractors)
    }
    
    var body: some View {
        VStack {
            TextField("Enter a title...", text: Binding(get: {
                viewModel.state.title
            }, set: { value in
                viewModel.onEvent(event: CreateDishEvent.EditTitle(title: value))
            })).padding(.horizontal)
                            .font(.title)
            MultilineTextField(text: Binding(
                get: {viewModel.state.desc},
                set: {value in
                    viewModel.onEvent(event: CreateDishEvent.EditDescription(description: value))}))
            Button(action: {
                viewModel.onEvent(event: CreateDishEvent.CreateDish(onCreate: {}))
            }) {
                Text("Save")
                    .font(.system(size: 18, weight: .medium))
                    .foregroundColor(.white)
            }
            .frame(minWidth: 0)
            .padding(10)
            .background(Color.blue)
            .cornerRadius(15)
        }
        .onAppear {
            viewModel.startObserving()
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}

struct CreateDishScreen_Previews: PreviewProvider {
    static var previews: some View {
        CreateDishScreen(dishInteractors: AppModule().dishInteractors)
    }
}
