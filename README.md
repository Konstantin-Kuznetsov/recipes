# recipes

https://user-images.githubusercontent.com/18750579/148676220-460076df-8d82-4100-8b17-358388d94c2c.mp4


Definition of this test task was taken from here https://github.com/mrabelwahed/HelloFresh-Challenge
I'm not sure that this is the actual test task in this company but it's very typical challenge so I decided to use API with data from that page. 
One screen with list of items and another one with details. Additional feature in the test task - a recipe can be favorited or unfavorited on details screen.

**Libraries**
----------

Room, Navigation, Retrofit, Dagger2, Coroutines, Gson, Glide, Swiperefreshlayout, Espresso, Mockito

**Gradle**
----------

Features are located in separate gradle modules

![image](https://user-images.githubusercontent.com/18750579/148232153-f16709b9-20dc-4010-91cf-d38c15cb44a5.png)

**DI (Dagger)** 
----------

Singletones in core module and screen-scoped dependencies for List and Details

CoreComponent is providing core dependencies for feature components as a component dependency (dependencies = [CoreComponent::class])

![image](https://user-images.githubusercontent.com/18750579/148233689-e3395334-c0c6-444c-9bab-97e2271f42e0.png)

**App architecture** 
----------

Clean architecture. Here is no separated usecases but one interactor for each screen with business logic

![image](https://user-images.githubusercontent.com/18750579/148501382-82bae152-5616-4f3a-a3ff-4bff67acb2a7.png)

**Presentation pattern** 
----------

Immutable single state and effects in ViewModel.
View is observing flow of states and effects and rendering the data.
It's a very smple pattern with data driven conception and unidirectional data flow.

The view creates events initiated by the user or Android lifecycle.

The ViewModel requests data from the repository.

The repository returns data from the network/database.

The ViewModel creates/updates the view state, comprising the UI, and effects, which are one-time events like navigation or error-handling.

The view observes the view states/effect changes and rendering views.

![image](https://user-images.githubusercontent.com/18750579/148502819-eba78d1b-d24e-408b-aae9-82dc17ff6946.png)

**Database and DAOs** 
----------

Room database with simple DAO
https://github.com/Konstantin-Kuznetsov/recipes/blob/main/core/src/main/java/com/example/core/data/database/dao/RecipesDao.kt

Some queries runs with @Transaction

I assume that content of recepies can be changed after the first caching in DB, so I replace content in every update from API. 
Favourited recipes stay favourited during this update, ofcourse.

Full recipe entity is building using joining two tables on recipe_id. I used here @Embedded and @Relation annotations for simplicity but it's exactly SQL joining under the hood.
https://github.com/Konstantin-Kuznetsov/recipes/blob/main/core/src/main/java/com/example/core/data/database/entities/FullRecipeInfo.kt

**Unit Tests** 
----------

Some of unit tests for mappers, interactors etc..

https://github.com/Konstantin-Kuznetsov/recipes/blob/main/feed/src/test/java/com/example/feed/domain/RecipesFeedInteractorImplTest.kt

https://github.com/Konstantin-Kuznetsov/recipes/blob/main/feed/src/test/java/com/example/feed/data/repo/RecipesFeedRepoImplTest.kt

https://github.com/Konstantin-Kuznetsov/recipes/blob/main/core/src/test/java/com/example/core/data/mapper/ApiResponseErrorMapperImplTest.kt

**Instrumented Tests** 
----------


Database tests, DAO methods tests using in-memory Room instance

https://github.com/Konstantin-Kuznetsov/recipes/blob/main/core/src/androidTest/java/com/example/core/data/database/RecipesDatabaseTest.kt

**UI Tests (Espresso)** 
----------

https://github.com/Konstantin-Kuznetsov/recipes/blob/main/app/src/androidTest/java/com/example/recipes/RecipesInstrumentedTest.kt
https://github.com/Konstantin-Kuznetsov/recipes/blob/main/app/src/androidTest/java/com/example/recipes/uiTestHelpers.kt
https://github.com/Konstantin-Kuznetsov/recipes/blob/main/app/src/androidTest/java/com/example/recipes/screens/RecipesListScreen.kt
https://github.com/Konstantin-Kuznetsov/recipes/blob/main/app/src/androidTest/java/com/example/recipes/screens/RecipeDetailsScreen.kt

test case:

1) Waiting for data loaded on main screen
2) Checking that item with given recipe header is not favourited yet
3) Navigating to details screen by clicking the item card with given recipe header
4) Clicking on "heart" button to add recipe to favourites on details screen
5) Navigating back to the recipes list
6) Checking that item is favourited in list after clicking on "heart" button on details

