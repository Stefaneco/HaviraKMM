//
//  MultilineTextField.swift
//  iosApp
//
//  Created by Piotr Kalin on 02/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct MultilineTextField : View {
    
    @Binding var text : String
    
    var body: some View {
        VStack(alignment: .leading) {
            TextEditor(text: $text)
                .frame(
                    maxWidth: .infinity,
                    minHeight: 100,
                    alignment: .topLeading
                )
                .cornerRadius(15)
                .padding()
                .shadow(radius: 4)
                .foregroundColor(Color.onSurface)
        }
    }
}

struct MultilineTextField_Previews : PreviewProvider {
    
    static var previews: some View {
        MultilineTextField(text: .constant("ABCDEF"))
    }
}
