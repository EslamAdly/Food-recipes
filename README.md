# Ratatouille - Food Recipe App

**Ratatouille** is a mobile app for food lovers to explore, search, and manage their favorite recipes. The app allows users to discover meals by category, country, or ingredients, create meal plans, and sync their data across devices using Firebase. Built using **Kotlin** and **Room Database**, Ratatouille ensures offline functionality for favorite meals and meal plans.

## Features

- **Meal of the Day**: Displays a random meal recommendation every day.
- **Search Meals**: Users can search for meals by:
  - Country
  - Ingredient
  - Category
- **Favorites Management**: Users can add or remove meals from their favorite list and save them offline using Room Database.
- **Meal Plan**: Users can plan their meals for each day of the week.
- **Ingredient Management**: View all ingredients and manage selected ingredients for meals.
- **Firebase Integration**:
  - Google Authentication
  - Save and sync favorite meals, meal plans, and ingredients for each user.
  - Data synchronization across devices.
- **Offline Access**: View favorite meals and meal plans even when offline.

## Technologies Used

- **Kotlin**: The app is written in Kotlin, following modern Android development practices.
- **Room Database**: For local storage of meals, ingredients, and meal plans.
- **Firebase**:
  - Authentication for user login and registration.
  - Firestore for syncing user data like favorite meals, meal plans, and selected ingredients.
- **MVVM Architecture**: The app uses the MVVM pattern to maintain a clean separation of concerns between UI and data.
## Screenshots

<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="https://github.com/user-attachments/assets/e3ff51e1-2565-454e-aa35-f7bc19a2ccea" alt="home" width="45%" />
  <img src="https://github.com/user-attachments/assets/6f59b97b-1a3e-48c4-af76-db99c1d478cf" alt="favorite screen" width="45%" />
  <img src="https://github.com/user-attachments/assets/1c629098-6b7e-458f-923d-8624aed93ee9" alt="meal screen" width="45%" />
  <img src="https://github.com/user-attachments/assets/7948cf91-a190-414a-bc46-8f48a1fe087d" alt="meal ingredient" width="45%" />
  <img src="https://github.com/user-attachments/assets/b4d48e30-cde4-414b-b84c-ae414233d815" alt="plan meals" width="45%" />
  <img src="https://github.com/user-attachments/assets/11740607-873c-4eec-a89b-9c13abad926b" alt="ingredient list" width="45%" />
</div>
