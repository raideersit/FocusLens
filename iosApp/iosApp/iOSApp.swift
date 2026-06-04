import SwiftUI
import shared

/// Punto de entrada de la app iOS de FocusLens.
/// Usa Compose Multiplatform renderizado dentro de un UIViewController.
@main
struct iOSApp: App {

    init() {
        // Inicializar Koin con el módulo iOS
        MainViewControllerKt.initKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

/// Wrapper SwiftUI que embebe el UIViewController de Compose Multiplatform.
struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
