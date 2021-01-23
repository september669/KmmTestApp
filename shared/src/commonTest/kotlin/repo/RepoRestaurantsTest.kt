package repo

import org.dda.testwork.shared.api.ChibbisService
import org.dda.testwork.shared.api.dto.Dish
import org.dda.testwork.shared.api.dto.RestaurantItem
import org.dda.testwork.shared.api.dto.RestaurantReview
import org.dda.testwork.shared.coroutine_context.runTest
import org.dda.testwork.shared.repo.RepoRestaurants
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class RepoRestaurantsTest {

    val emptyItem = RestaurantItem(
        deliveryCost = 0,
        deliveryTime = 0,
        logoUrl = "logoUrl",
        minCost = 0,
        name = "name",
        positiveReviews = 0,
        reviewsCount = 0,
        specializations = emptyList()
    )


    private val service = object : ChibbisService {
        override suspend fun getRestaurantList(): List<RestaurantItem> {
            return listOf(
                emptyItem,
                emptyItem.copy(name = "name1"),
                emptyItem.copy(name = "name2")
            )
        }

        override suspend fun getDishHits(): List<Dish> {
            TODO("Not yet implemented")
        }

        override suspend fun getRestaurantReviews(): List<RestaurantReview> {
            TODO("Not yet implemented")
        }
    }

    /*
        TODO dda: mockk still not working on ios target
        private val service = mockk<ChibbisService> {
            coEvery { getRestaurantList() } returns listOf(
                emptyItem,
                emptyItem.copy(name = "name1"),
                emptyItem.copy(name = "name2")
            )
        }

    */
    lateinit var repo: RepoRestaurants

    @BeforeTest
    fun setup() {
        repo = RepoRestaurants(service)
    }

    @Test
    fun emptyQuery() = runTest {
        val list = repo.findRestaurantList("")
        assertTrue { list.size == 3 }
        assertEquals(expected = emptyItem, actual = list[0])
        assertEquals(expected = emptyItem.copy(name = "name1"), actual = list[1])
        assertEquals(expected = emptyItem.copy(name = "name2"), actual = list[2])
    }

    @Test
    fun blankQuery() = runTest {
        val list = repo.findRestaurantList("    ")
        assertTrue { list.size == 3 }
        assertEquals(expected = emptyItem, actual = list[0])
        assertEquals(expected = emptyItem.copy(name = "name1"), actual = list[1])
        assertEquals(expected = emptyItem.copy(name = "name2"), actual = list[2])
    }

    @Test
    fun queryName1() = runTest {
        val list = repo.findRestaurantList("name1")
        assertTrue { list.size == 1 }
        assertEquals(expected = emptyItem.copy(name = "name1"), actual = list[0])
    }

    @Test
    fun queryNAME1() = runTest {
        val list = repo.findRestaurantList("NAME1")
        assertTrue { list.size == 1 }
        assertEquals(expected = emptyItem.copy(name = "name1"), actual = list[0])
    }

    @Test
    fun emptyResult() = runTest {
        val list = repo.findRestaurantList("WsadawW")
        assertTrue { list.isEmpty() }
    }

}