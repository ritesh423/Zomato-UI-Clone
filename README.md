# 🍔 Zomato UI Clone (Jetpack Compose)

A modern **Zomato inspired food delivery app clone** built using **Jetpack Compose** with complete offline functionality using Room database, DataStore preferences, and authentic Zomato design system.

This project demonstrates a full-featured food delivery application with user authentication, restaurant browsing, cart management, order placement, and admin dashboard.

---

## 📱 App Preview

<img width="1536" height="1024" alt="Zomato-Clone-Screens" src="https://github.com/user-attachments/assets/60471863-a440-4e05-910d-7c5dc85e90bb" />

---

## 🚀 Features

### User Features
* **Authentication** - Login, Signup, Forgot Password with session persistence
* **Onboarding** - 3-page animated onboarding experience
* **Home Feed** - Offer banners, categories, restaurant listings
* **Restaurant Detail** - Menu browsing with categories, ratings, reviews
* **Search** - Search restaurants and dishes with filters (veg only, rating, delivery time)
* **Cart Management** - Add/remove items, quantity stepper, coupon codes
* **Checkout** - Address selection, payment method, order summary
* **Order History** - View past orders, reorder functionality
* **Wishlist** - Save favorite restaurants
* **Profile** - Edit profile, dark mode toggle, logout

### Admin Features
* **Dashboard** - Stats overview (orders, revenue, restaurants, users)
* **Recent Orders** - View and manage recent orders
* **Quick Actions** - Manage restaurants, menu, banners, users

### Design System
* **Zomato Color Palette** - Authentic #E23744 red, rating green, etc.
* **Custom Components** - RestaurantCard, MenuItemRow, CategoryChip, FloatingCartBar
* **Animations** - Heart toggle, cart stepper, shimmer loading, screen transitions
* **Dark Mode** - Full dark theme support

---

## 🔐 Test Credentials

| Role  | Email              | Password  |
|-------|-------------------|-----------|
| User  | user@food.com     | user123   |
| Admin | admin@food.com    | admin123  |

---

## 🛠️ Tech Stack

| Technology             | Usage                          |
| ---------------------- | ------------------------------ |
| **Kotlin**             | Programming language           |
| **Jetpack Compose**    | Modern UI toolkit              |
| **Material 3**         | UI Components                  |
| **Room Database**      | Local data persistence         |
| **DataStore**          | Preferences & session storage  |
| **Hilt**               | Dependency injection           |
| **Coil**               | Image loading                  |
| **Coroutines + Flow**  | Async operations               |
| **Navigation Compose** | Type-safe navigation           |
| **Paging 3**           | Pagination support             |
| **Splash Screen API**  | Launch screen                  |

---

## 📂 Project Structure

```
app/src/main/java/com/riteshapps/zomatoclone/
├── common/                     # Shared utilities (UiState, ResultState)
├── data/
│   ├── dataModule/             # Hilt dependency injection modules
│   ├── local/
│   │   ├── dao/                # Room DAOs
│   │   ├── datastore/          # DataStore classes
│   │   ├── entity/             # Room entities
│   │   ├── AppDatabase.kt      # Room database
│   │   └── DatabaseSeeder.kt   # Mock data seeder
│   └── repository/             # Repository implementations
├── domain/model/               # Domain models
├── presentation/
│   ├── components/             # Reusable UI components
│   ├── navigation/             # NavHost, Routes
│   ├── screens/                # Screen composables
│   └── viewmodel/              # ViewModels
└── ui/theme/                   # Colors, Typography, Theme
```

---

## ▶️ How to Run

1. **Clone the repository**
```bash
git clone https://github.com/your-username/zomato-ui-clone.git
```

2. **Open in Android Studio** (Hedgehog or newer recommended)

3. **Sync Gradle** and wait for dependencies to download

4. **Run the app** on:
   - Emulator (API 26+)
   - Physical device

5. **Login** with test credentials:
   - User: `user@food.com` / `user123`
   - Admin: `admin@food.com` / `admin123`

---

## 🎨 Coupon Codes

The app supports these coupon codes in checkout:

| Code     | Discount           |
|----------|-------------------|
| SAVE10   | 10% off            |
| FIRST50  | ₹50 off            |
| FREEDEL  | Free delivery      |

---

## 🌙 Dark Mode

Toggle dark mode from:
- Profile Screen → Dark Mode switch

---

## 📱 Screens Overview

### Authentication
- Splash Screen (animated logo)
- Onboarding (3 pages with HorizontalPager)
- Login Screen
- Signup Screen
- Forgot Password Screen

### Main App
- Home/Delivery Screen (banners, categories, restaurants)
- Restaurant Detail Screen (collapsing toolbar, menu, reviews)
- Search Screen (filters, recent searches)
- Cart Screen (items, coupons, bill details)
- Checkout Screen (address, payment, order summary)
- Order Success Screen (animated checkmark)
- Orders Screen (order history)
- Wishlist Screen (saved restaurants)
- Profile Screen (settings, logout)
- Edit Profile Screen

### Admin
- Admin Dashboard (stats, recent orders, quick actions)

---

## 🧠 Key Concepts Implemented

* **MVVM Architecture** - ViewModels with StateFlow
* **Repository Pattern** - Clean data layer abstraction
* **Room Database** - 11 entities with DAOs
* **Flow-based Queries** - Reactive UI updates
* **Dependency Injection** - Hilt for DI
* **Type-safe Navigation** - Kotlinx Serialization routes
* **Composable State Hoisting** - Stateless UI components
* **Material 3 Design** - Modern UI components
* **Custom Animations** - Spring, tween, infinite transitions

---

## 📚 Dependencies

```kotlin
// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// DataStore
implementation("androidx.datastore:datastore-preferences:1.1.1")

// Hilt
implementation("com.google.dagger:hilt-android:2.51.1")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Coil
implementation("io.coil-kt:coil-compose:2.6.0")

// Paging 3
implementation("androidx.paging:paging-compose:3.3.2")

// Splash Screen
implementation("androidx.core:core-splashscreen:1.0.1")
```

---

## 📝 Notes

- This is a **fully offline app** - no network calls, all data from Room
- Database is seeded on first launch with mock restaurants, menu items, users
- Password hashing uses SHA-256 via `java.security.MessageDigest`
- Cart clears if adding items from a different restaurant (same as real Zomato)

---

## 👨‍💻 Author

**Ritesh Singh**

* Android Developer
* Passionate about Kotlin & Jetpack Compose

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
