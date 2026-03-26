package com.riteshapps.zomatoclone.data.local

import com.riteshapps.zomatoclone.R
import com.riteshapps.zomatoclone.data.local.dao.*
import com.riteshapps.zomatoclone.data.local.entity.*
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor(
    private val userDao: UserDao,
    private val categoryDao: CategoryDao,
    private val restaurantDao: RestaurantDao,
    private val menuItemDao: MenuItemDao,
    private val bannerDao: BannerDao,
    private val reviewDao: ReviewDao,
    private val addressDao: AddressDao
) {
    suspend fun seedIfEmpty() {
        if (categoryDao.getCategoryCount() > 0) return

        seedUsers()
        seedCategories()
        seedBanners()
        seedRestaurants()
        seedMenuItems()
        seedReviews()
        seedAddresses()
    }

    private suspend fun seedUsers() {
        val users = listOf(
            UserEntity(
                name = "Admin User",
                email = "admin@food.com",
                passwordHash = hashPassword("admin123"),
                phone = "9876543210",
                isAdmin = true
            ),
            UserEntity(
                name = "Test User",
                email = "user@food.com",
                passwordHash = hashPassword("user123"),
                phone = "9876543211",
                isAdmin = false
            )
        )
        users.forEach { userDao.insertUser(it) }
    }

    private suspend fun seedCategories() {
        val categories = listOf(
            CategoryEntity(id = 1, name = "Biryani", iconResId = R.drawable.vegbiryani),
            CategoryEntity(id = 2, name = "Pizza", iconResId = R.drawable.pizza_image),
            CategoryEntity(id = 3, name = "Burger", iconResId = R.drawable.burger),
            CategoryEntity(id = 4, name = "Chinese", iconResId = R.drawable.chinese),
            CategoryEntity(id = 5, name = "Thali", iconResId = R.drawable.allfood),
            CategoryEntity(id = 6, name = "Desserts", iconResId = R.drawable.ice_cream),
            CategoryEntity(id = 7, name = "South Indian", iconResId = R.drawable.allfood),
            CategoryEntity(id = 8, name = "Rolls", iconResId = R.drawable.rolls)
        )
        categoryDao.insertCategories(categories)
    }

    private suspend fun seedBanners() {
        val banners = listOf(
            BannerEntity(
                imageResId = R.drawable.deliverybanner,
                title = "Free Delivery",
                subtitle = "On orders above ₹199",
                offerText = "FREE DELIVERY"
            ),
            BannerEntity(
                imageResId = R.drawable.diningbanner,
                title = "Flat 50% OFF",
                subtitle = "On your first order",
                offerText = "50% OFF"
            ),
            BannerEntity(
                imageResId = R.drawable.quickbanner,
                title = "Extra ₹25 OFF",
                subtitle = "Use code: SAVE25",
                offerText = "₹25 OFF"
            )
        )
        bannerDao.insertBanners(banners)
    }

    private suspend fun seedRestaurants() {
        val restaurants = listOf(
            // Biryani Restaurants
            RestaurantEntity(
                id = 1,
                name = "Paradise Biryani",
                cuisine = "Biryani, North Indian, Mughlai",
                rating = 4.5f,
                ratingCount = 1250,
                deliveryTime = "30-40 min",
                minOrder = 150,
                deliveryFee = 30,
                distance = "2.5 km",
                imageResId = R.drawable.veg_biryani,
                categoryId = 1,
                offerTag = "₹50 OFF above ₹249",
                address = "Connaught Place, Delhi"
            ),
            RestaurantEntity(
                id = 2,
                name = "Behrouz Biryani",
                cuisine = "Biryani, Mughlai, North Indian",
                rating = 4.3f,
                ratingCount = 980,
                deliveryTime = "35-45 min",
                minOrder = 200,
                deliveryFee = 25,
                distance = "3.2 km",
                imageResId = R.drawable.veg_biryani,
                categoryId = 1,
                offerTag = "20% OFF",
                address = "Karol Bagh, Delhi"
            ),
            // Pizza Restaurants
            RestaurantEntity(
                id = 3,
                name = "Domino's Pizza",
                cuisine = "Pizza, Italian, Fast Food",
                rating = 4.2f,
                ratingCount = 2100,
                deliveryTime = "25-35 min",
                minOrder = 100,
                deliveryFee = 20,
                distance = "1.8 km",
                imageResId = R.drawable.brick_oven_pizza,
                categoryId = 2,
                offerTag = "Buy 1 Get 1 Free",
                address = "Rajouri Garden, Delhi"
            ),
            RestaurantEntity(
                id = 4,
                name = "Pizza Hut",
                cuisine = "Pizza, Italian, Beverages",
                rating = 4.1f,
                ratingCount = 1560,
                deliveryTime = "30-40 min",
                minOrder = 150,
                deliveryFee = 25,
                distance = "2.1 km",
                imageResId = R.drawable.pepperoni_pizza,
                categoryId = 2,
                offerTag = "Flat 30% OFF",
                address = "Dwarka, Delhi"
            ),
            // Burger Restaurants
            RestaurantEntity(
                id = 5,
                name = "Burger King",
                cuisine = "Burgers, American, Fast Food",
                rating = 4.0f,
                ratingCount = 1890,
                deliveryTime = "20-30 min",
                minOrder = 100,
                deliveryFee = 15,
                distance = "1.5 km",
                imageResId = R.drawable.burger,
                categoryId = 3,
                offerTag = "Free Fries",
                address = "Nehru Place, Delhi"
            ),
            RestaurantEntity(
                id = 6,
                name = "McDonald's",
                cuisine = "Burgers, Fast Food, American",
                rating = 4.2f,
                ratingCount = 3200,
                deliveryTime = "15-25 min",
                minOrder = 99,
                deliveryFee = 20,
                distance = "0.8 km",
                imageResId = R.drawable.burger3,
                categoryId = 3,
                offerTag = "Combo at ₹149",
                address = "Saket, Delhi"
            ),
            // Chinese Restaurants
            RestaurantEntity(
                id = 7,
                name = "Mainland China",
                cuisine = "Chinese, Asian, Pan-Asian",
                rating = 4.4f,
                ratingCount = 890,
                deliveryTime = "35-45 min",
                minOrder = 300,
                deliveryFee = 35,
                distance = "4.0 km",
                imageResId = R.drawable.chowmein1,
                categoryId = 4,
                offerTag = "15% OFF",
                address = "Vasant Kunj, Delhi"
            ),
            RestaurantEntity(
                id = 8,
                name = "Yo! China",
                cuisine = "Chinese, Fast Food, Asian",
                rating = 3.9f,
                ratingCount = 720,
                deliveryTime = "25-35 min",
                minOrder = 150,
                deliveryFee = 25,
                distance = "2.8 km",
                imageResId = R.drawable.spring_roll,
                categoryId = 4,
                offerTag = "20% OFF",
                address = "Lajpat Nagar, Delhi"
            ),
            // Thali Restaurants
            RestaurantEntity(
                id = 9,
                name = "Haldiram's",
                cuisine = "North Indian, South Indian, Thali",
                rating = 4.3f,
                ratingCount = 2500,
                deliveryTime = "25-35 min",
                minOrder = 100,
                deliveryFee = 20,
                distance = "1.2 km",
                imageResId = R.drawable.allfood,
                categoryId = 5,
                offerTag = "₹100 OFF above ₹499",
                address = "Chandni Chowk, Delhi",
                isVegOnly = true
            ),
            // Dessert Restaurants
            RestaurantEntity(
                id = 10,
                name = "Baskin Robbins",
                cuisine = "Ice Cream, Desserts, Beverages",
                rating = 4.5f,
                ratingCount = 1100,
                deliveryTime = "20-30 min",
                minOrder = 100,
                deliveryFee = 25,
                distance = "1.9 km",
                imageResId = R.drawable.ice_cream,
                categoryId = 6,
                offerTag = "Free Scoop",
                address = "Greater Kailash, Delhi"
            ),
            // South Indian
            RestaurantEntity(
                id = 11,
                name = "Saravana Bhavan",
                cuisine = "South Indian, Dosa, Idli",
                rating = 4.4f,
                ratingCount = 1800,
                deliveryTime = "25-35 min",
                minOrder = 100,
                deliveryFee = 15,
                distance = "2.0 km",
                imageResId = R.drawable.allfood,
                categoryId = 7,
                offerTag = "10% OFF",
                address = "Janpath, Delhi",
                isVegOnly = true
            ),
            // Rolls
            RestaurantEntity(
                id = 12,
                name = "Kathi Roll Junction",
                cuisine = "Rolls, Fast Food, North Indian",
                rating = 4.1f,
                ratingCount = 650,
                deliveryTime = "20-30 min",
                minOrder = 80,
                deliveryFee = 15,
                distance = "1.6 km",
                imageResId = R.drawable.rolls,
                categoryId = 8,
                offerTag = "Buy 2 Get 1",
                address = "Connaught Place, Delhi"
            )
        )
        restaurantDao.insertRestaurants(restaurants)
    }

    private suspend fun seedMenuItems() {
        val menuItems = listOf(
            // Paradise Biryani (Restaurant 1)
            MenuItemEntity(restaurantId = 1, name = "Chicken Dum Biryani", description = "Aromatic basmati rice cooked with tender chicken pieces and special spices", price = 299, imageResId = R.drawable.veg_biryani, isVeg = false, category = "Main Course", rating = 4.5f, isBestSeller = true),
            MenuItemEntity(restaurantId = 1, name = "Mutton Biryani", description = "Slow-cooked mutton with fragrant rice and secret spices", price = 399, imageResId = R.drawable.veg_biryani, isVeg = false, category = "Main Course", rating = 4.6f),
            MenuItemEntity(restaurantId = 1, name = "Veg Biryani", description = "Mixed vegetables cooked with basmati rice and aromatic spices", price = 199, imageResId = R.drawable.veg_biryani, isVeg = true, category = "Main Course", rating = 4.2f),
            MenuItemEntity(restaurantId = 1, name = "Chicken Seekh Kebab", description = "Minced chicken kebabs grilled to perfection", price = 229, imageResId = R.drawable.placeholder_food, isVeg = false, category = "Starters", rating = 4.3f),
            MenuItemEntity(restaurantId = 1, name = "Paneer Tikka", description = "Marinated cottage cheese cubes grilled in tandoor", price = 199, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Starters", rating = 4.4f, isBestSeller = true),
            MenuItemEntity(restaurantId = 1, name = "Gulab Jamun", description = "Soft milk dumplings soaked in sugar syrup", price = 89, imageResId = R.drawable.sweets, isVeg = true, category = "Desserts", rating = 4.5f),
            MenuItemEntity(restaurantId = 1, name = "Lassi", description = "Traditional yogurt-based sweet drink", price = 79, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Beverages", rating = 4.3f),
            
            // Domino's Pizza (Restaurant 3)
            MenuItemEntity(restaurantId = 3, name = "Margherita Pizza", description = "Classic cheese pizza with tomato sauce and oregano", price = 199, imageResId = R.drawable.brick_oven_pizza, isVeg = true, category = "Pizza", rating = 4.2f, isBestSeller = true),
            MenuItemEntity(restaurantId = 3, name = "Pepperoni Pizza", description = "Loaded with spicy pepperoni and mozzarella cheese", price = 349, imageResId = R.drawable.pepperoni_pizza, isVeg = false, category = "Pizza", rating = 4.5f),
            MenuItemEntity(restaurantId = 3, name = "Farmhouse Pizza", description = "Bell peppers, onions, mushrooms, and corn on a cheesy base", price = 299, imageResId = R.drawable.brick_oven_pizza, isVeg = true, category = "Pizza", rating = 4.3f),
            MenuItemEntity(restaurantId = 3, name = "Garlic Breadsticks", description = "Freshly baked bread with garlic butter and herbs", price = 99, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Sides", rating = 4.4f, isBestSeller = true),
            MenuItemEntity(restaurantId = 3, name = "Choco Lava Cake", description = "Warm chocolate cake with molten center", price = 109, imageResId = R.drawable.sweets, isVeg = true, category = "Desserts", rating = 4.6f),
            MenuItemEntity(restaurantId = 3, name = "Pepsi", description = "Chilled soft drink 500ml", price = 60, imageResId = R.drawable.coke, isVeg = true, category = "Beverages", rating = 4.0f),
            
            // Burger King (Restaurant 5)
            MenuItemEntity(restaurantId = 5, name = "Whopper", description = "Flame-grilled beef patty with fresh vegetables and signature sauce", price = 199, imageResId = R.drawable.burger, isVeg = false, category = "Burgers", rating = 4.4f, isBestSeller = true),
            MenuItemEntity(restaurantId = 5, name = "Veg Whopper", description = "Crispy veg patty with fresh lettuce, tomatoes, and mayo", price = 149, imageResId = R.drawable.burger, isVeg = true, category = "Burgers", rating = 4.2f),
            MenuItemEntity(restaurantId = 5, name = "Chicken Royale", description = "Crispy chicken patty with creamy mayo and fresh lettuce", price = 179, imageResId = R.drawable.burger3, isVeg = false, category = "Burgers", rating = 4.3f),
            MenuItemEntity(restaurantId = 5, name = "French Fries (Large)", description = "Golden crispy fries with seasoning", price = 99, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Sides", rating = 4.5f, isBestSeller = true),
            MenuItemEntity(restaurantId = 5, name = "Onion Rings", description = "Crispy battered onion rings", price = 89, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Sides", rating = 4.1f),
            MenuItemEntity(restaurantId = 5, name = "Chocolate Shake", description = "Thick creamy chocolate milkshake", price = 129, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Beverages", rating = 4.4f),
            
            // Mainland China (Restaurant 7)
            MenuItemEntity(restaurantId = 7, name = "Veg Hakka Noodles", description = "Stir-fried noodles with vegetables in Chinese sauces", price = 199, imageResId = R.drawable.chowmein1, isVeg = true, category = "Main Course", rating = 4.3f, isBestSeller = true),
            MenuItemEntity(restaurantId = 7, name = "Chicken Manchurian", description = "Deep-fried chicken in spicy Manchurian gravy", price = 279, imageResId = R.drawable.placeholder_food, isVeg = false, category = "Main Course", rating = 4.5f),
            MenuItemEntity(restaurantId = 7, name = "Spring Rolls (4 pcs)", description = "Crispy rolls stuffed with vegetables", price = 149, imageResId = R.drawable.spring_roll, isVeg = true, category = "Starters", rating = 4.4f, isBestSeller = true),
            MenuItemEntity(restaurantId = 7, name = "Dim Sum (6 pcs)", description = "Steamed dumplings with savory filling", price = 199, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Starters", rating = 4.2f),
            MenuItemEntity(restaurantId = 7, name = "Fried Rice", description = "Wok-tossed rice with vegetables and eggs", price = 179, imageResId = R.drawable.placeholder_food, isVeg = false, category = "Main Course", rating = 4.3f),
            MenuItemEntity(restaurantId = 7, name = "Green Tea", description = "Refreshing Chinese green tea", price = 79, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Beverages", rating = 4.0f),
            
            // Haldiram's (Restaurant 9)
            MenuItemEntity(restaurantId = 9, name = "Special Thali", description = "Complete meal with dal, sabzi, roti, rice, and sweet", price = 249, imageResId = R.drawable.allfood, isVeg = true, category = "Main Course", rating = 4.5f, isBestSeller = true),
            MenuItemEntity(restaurantId = 9, name = "Chole Bhature", description = "Spicy chickpea curry with fluffy fried bread", price = 149, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Main Course", rating = 4.4f),
            MenuItemEntity(restaurantId = 9, name = "Paneer Butter Masala", description = "Cottage cheese in rich tomato-butter gravy", price = 219, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Main Course", rating = 4.3f, isBestSeller = true),
            MenuItemEntity(restaurantId = 9, name = "Samosa (2 pcs)", description = "Crispy potato-filled triangular pastry", price = 49, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Starters", rating = 4.6f),
            MenuItemEntity(restaurantId = 9, name = "Rasgulla (2 pcs)", description = "Soft cottage cheese balls in sugar syrup", price = 69, imageResId = R.drawable.sweets, isVeg = true, category = "Desserts", rating = 4.5f),
            MenuItemEntity(restaurantId = 9, name = "Mango Lassi", description = "Sweet yogurt drink with mango pulp", price = 89, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Beverages", rating = 4.4f),
            
            // Baskin Robbins (Restaurant 10)
            MenuItemEntity(restaurantId = 10, name = "Chocolate Fudge Sundae", description = "Rich chocolate ice cream with hot fudge and nuts", price = 179, imageResId = R.drawable.ice_cream, isVeg = true, category = "Sundaes", rating = 4.6f, isBestSeller = true),
            MenuItemEntity(restaurantId = 10, name = "Mango Tango Scoop", description = "Fresh mango flavored ice cream", price = 99, imageResId = R.drawable.ice_cream, isVeg = true, category = "Scoops", rating = 4.4f),
            MenuItemEntity(restaurantId = 10, name = "Belgian Chocolate", description = "Premium chocolate ice cream with chunks", price = 119, imageResId = R.drawable.ice_cream, isVeg = true, category = "Scoops", rating = 4.5f),
            MenuItemEntity(restaurantId = 10, name = "Brownie Sundae", description = "Warm brownie topped with vanilla ice cream", price = 199, imageResId = R.drawable.ice_cream, isVeg = true, category = "Sundaes", rating = 4.7f, isBestSeller = true),
            MenuItemEntity(restaurantId = 10, name = "Ice Cream Shake", description = "Thick milkshake made with ice cream", price = 149, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Beverages", rating = 4.3f),
            
            // Add items for remaining restaurants
            MenuItemEntity(restaurantId = 2, name = "Lucknowi Biryani", description = "Awadhi style biryani with tender meat", price = 349, imageResId = R.drawable.veg_biryani, isVeg = false, category = "Main Course", rating = 4.4f, isBestSeller = true),
            MenuItemEntity(restaurantId = 2, name = "Paneer Biryani", description = "Cottage cheese biryani with mint raita", price = 249, imageResId = R.drawable.veg_biryani, isVeg = true, category = "Main Course", rating = 4.2f),
            
            MenuItemEntity(restaurantId = 4, name = "Supreme Pizza", description = "Loaded with veggies and cheese", price = 399, imageResId = R.drawable.brick_oven_pizza, isVeg = true, category = "Pizza", rating = 4.3f, isBestSeller = true),
            MenuItemEntity(restaurantId = 4, name = "Chicken Tikka Pizza", description = "Indian style chicken tikka topping", price = 449, imageResId = R.drawable.pepperoni_pizza, isVeg = false, category = "Pizza", rating = 4.5f),
            
            MenuItemEntity(restaurantId = 6, name = "McChicken", description = "Crispy chicken patty burger", price = 149, imageResId = R.drawable.burger3, isVeg = false, category = "Burgers", rating = 4.2f, isBestSeller = true),
            MenuItemEntity(restaurantId = 6, name = "McAloo Tikki", description = "Spiced potato patty burger", price = 59, imageResId = R.drawable.burger, isVeg = true, category = "Burgers", rating = 4.0f),
            
            MenuItemEntity(restaurantId = 8, name = "Schezwan Noodles", description = "Spicy noodles with schezwan sauce", price = 169, imageResId = R.drawable.chowmein1, isVeg = true, category = "Main Course", rating = 4.1f, isBestSeller = true),
            MenuItemEntity(restaurantId = 8, name = "Crispy Chilli Potato", description = "Fried potatoes in spicy sauce", price = 129, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Starters", rating = 4.3f),
            
            MenuItemEntity(restaurantId = 11, name = "Masala Dosa", description = "Crispy crepe with spiced potato filling", price = 99, imageResId = R.drawable.allfood, isVeg = true, category = "Main Course", rating = 4.5f, isBestSeller = true),
            MenuItemEntity(restaurantId = 11, name = "Idli Sambar (2 pcs)", description = "Steamed rice cakes with lentil curry", price = 79, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Main Course", rating = 4.4f),
            MenuItemEntity(restaurantId = 11, name = "Filter Coffee", description = "Traditional South Indian coffee", price = 49, imageResId = R.drawable.placeholder_food, isVeg = true, category = "Beverages", rating = 4.6f),
            
            MenuItemEntity(restaurantId = 12, name = "Chicken Kathi Roll", description = "Chicken tikka wrapped in paratha", price = 129, imageResId = R.drawable.rolls, isVeg = false, category = "Rolls", rating = 4.3f, isBestSeller = true),
            MenuItemEntity(restaurantId = 12, name = "Paneer Tikka Roll", description = "Grilled paneer wrapped in paratha", price = 109, imageResId = R.drawable.rolls, isVeg = true, category = "Rolls", rating = 4.2f),
            MenuItemEntity(restaurantId = 12, name = "Egg Roll", description = "Egg omelette wrapped in paratha", price = 89, imageResId = R.drawable.rolls, isVeg = false, category = "Rolls", rating = 4.1f)
        )
        menuItemDao.insertMenuItems(menuItems)
    }

    private suspend fun seedReviews() {
        val reviews = listOf(
            // Paradise Biryani reviews
            ReviewEntity(restaurantId = 1, userId = 2, userName = "Test User", rating = 4.5f, comment = "Amazing biryani! Best I've had in the city. The rice was perfectly cooked and the chicken was tender."),
            ReviewEntity(restaurantId = 1, userId = 2, userName = "Food Lover", rating = 5.0f, comment = "Absolutely delicious! Worth every penny. Will definitely order again."),
            ReviewEntity(restaurantId = 1, userId = 2, userName = "Biryani Fan", rating = 4.0f, comment = "Good taste but delivery was a bit late. Food was still warm though."),
            
            // Domino's reviews
            ReviewEntity(restaurantId = 3, userId = 2, userName = "Pizza Lover", rating = 4.0f, comment = "Good pizza, quick delivery. Garlic breadsticks were amazing!"),
            ReviewEntity(restaurantId = 3, userId = 2, userName = "Test User", rating = 4.5f, comment = "Fresh and hot pizza delivered in 25 minutes. Great service!"),
            
            // Burger King reviews
            ReviewEntity(restaurantId = 5, userId = 2, userName = "Burger Buff", rating = 4.5f, comment = "Whopper is the best! Perfectly grilled patty with fresh veggies."),
            ReviewEntity(restaurantId = 5, userId = 2, userName = "Fast Food Fan", rating = 4.0f, comment = "Consistent quality and taste. Fries were crispy and hot."),
            
            // Mainland China reviews
            ReviewEntity(restaurantId = 7, userId = 2, userName = "Asian Food Lover", rating = 4.5f, comment = "Authentic Chinese flavors! Spring rolls were crispy and delicious."),
            ReviewEntity(restaurantId = 7, userId = 2, userName = "Noodle Enthusiast", rating = 4.0f, comment = "Great noodles and the portion size is generous."),
            
            // Haldiram's reviews
            ReviewEntity(restaurantId = 9, userId = 2, userName = "Desi Food Fan", rating = 4.5f, comment = "Best thali in town! Everything was fresh and tasty."),
            ReviewEntity(restaurantId = 9, userId = 2, userName = "Test User", rating = 4.0f, comment = "Love their samosas and sweets. Perfect for family meals."),
            
            // Baskin Robbins reviews
            ReviewEntity(restaurantId = 10, userId = 2, userName = "Ice Cream Lover", rating = 5.0f, comment = "Best ice cream ever! The chocolate fudge sundae is to die for."),
            ReviewEntity(restaurantId = 10, userId = 2, userName = "Dessert Fan", rating = 4.5f, comment = "Great variety of flavors. Fresh and creamy ice cream.")
        )
        reviewDao.insertReviews(reviews)
    }

    private suspend fun seedAddresses() {
        val addresses = listOf(
            AddressEntity(
                userId = 2,
                label = "Home",
                fullAddress = "565, Shiv Vihar, Bahrampur, Shanti Nagar, Delhi - 110001",
                landmark = "Near Metro Station",
                isDefault = true
            ),
            AddressEntity(
                userId = 2,
                label = "Work",
                fullAddress = "Tower B, DLF Cyber City, Sector 24, Gurugram - 122002",
                landmark = "Opposite to Metro Station"
            )
        )
        addresses.forEach { addressDao.insertAddress(it) }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
