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
<div style="display: flex; flex-wrap: wrap; gap: 20px;">
  <img src="https://github.com/user-attachments/assets/191b4945-468c-4b95-a546-9ec16fa0f921" alt="home" width="45%" />
  <img src="https://github.com/user-attachments/assets/160f3835-75de-4aa0-81a2-1fd808c9e9ad" alt="favorite screen" width="45%" />
  <img src="https://github.com/user-attachments/assets/d542817a-2d3f-4d58-93d8-5970a2b261b8" alt="meal screen" width="45%" />
  <img src="https://github.com/user-attachments/assets/291221b9-9d32-4b01-9eaa-dbbd6ff0fc72" alt="meal ingredient" width="45%" />
  <img src="https://github.com/user-attachments/assets/57f0d912-5ba5-4566-aeca-3dfc840f2632" alt="plan meals" width="45%" />
  <img src="https://github.com/user-attachments/assets/aef2f5de-d65b-4c7c-855b-df3e09a90c5d" alt="ingredient list" width="45%" />
</div>
