package com.example.next_gen_recipe_app.data.remote.dto

/**
 * Data transfer object representing the response from the Spoonacular API's complex search endpoint.
 *
 * When you call:
 * https://api.spoonacular.com/recipes/complexSearch?query=chicken&number=10&addRecipeInformation=true&apiKey=YOUR_API_KEY
 *
 * The expected JSON response structure is:
 * {
 *    "results": [
 *        {
 *            "id": 635675,
 *            "image": "https://img.spoonacular.com/recipes/635675-312x231.jpg",
 *            "imageType": "jpg",
 *            "title": "Boozy Bbq Chicken",
 *            "readyInMinutes": 45,
 *            "servings": 6,
 *            "sourceUrl": "http://www.foodista.com/recipe/2ZPN8F4S/boozy-bbq-chicken",
 *            "vegetarian": false,
 *            "vegan": false,
 *            "glutenFree": true,
 *            "dairyFree": true,
 *            "veryHealthy": false,
 *            "cheap": false,
 *            "veryPopular": false,
 *            "sustainable": false,
 *            "lowFodmap": false,
 *            "weightWatcherSmartPoints": 20,
 *            "gaps": "no",
 *            "preparationMinutes": null,
 *            "cookingMinutes": null,
 *            "aggregateLikes": 72,
 *            "healthScore": 37.0,
 *            "creditsText": "Foodista.com – The Cooking Encyclopedia Everyone Can Edit",
 *            "license": "CC BY 3.0",
 *            "sourceName": "Foodista",
 *            "pricePerServing": 332.73,
 *            "summary": "Boozy Bbq Chicken could be just the <b>gluten free and dairy free</b> recipe you've been looking for...",
 *            "cuisines": [],
 *            "dishTypes": ["lunch", "main course", "main dish", "dinner"],
 *            "diets": ["gluten free", "dairy free"],
 *            "occasions": [],
 *            "spoonacularScore": 93.60807800292969,
 *            "spoonacularSourceUrl": "https://spoonacular.com/boozy-bbq-chicken-635675"
 *        },
 *        ...
 *    ],
 *    "offset": 0,
 *    "number": 10,
 *    "totalResults": 730
 * }
 *
 * Each recipe object in "results" is mapped to a [RecipeSearchResultDto]. Ensure that
 * your [RecipeSearchResultDto] includes all fields you need from the JSON response.
 */
data class RecipeSearchResponse(
    val results: List<RecipeSearchResultDto>, // List of search result DTOs
    val offset: Int,
    val number: Int,
    val totalResults: Int
)
