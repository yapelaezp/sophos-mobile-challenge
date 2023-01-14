package com.example.sophos_mobile_app.ui.offices

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sophos_mobile_app.MainCoroutineRule
import com.example.sophos_mobile_app.data.model.Office
import com.example.sophos_mobile_app.data.repository.FakeOfficeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class OfficesViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var officesViewModel: OfficesViewModel

    @Before
    fun setup() {
        val fakeOfficeRepository = FakeOfficeRepository()
        officesViewModel = OfficesViewModel(fakeOfficeRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getOffices_returnNonEmptyListOfOffices() = mainCoroutineRule.runBlockingTest {
        //Arrange
        officesViewModel.getOffices()
        //Action
        val actual = officesViewModel.offices.value?.size
        //Assert
        assert(actual!! > 0 )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getOffices_contentOfASingleOffice() = runBlockingTest {
        //Arrange
        officesViewModel.getOffices()
        //Action
        val expected = Office("Chile", 7, "-70.64851440000001",
            "33.440570099999995", "Agustinas 833 – Piso 10")
        val actual =  officesViewModel.offices.value?.first()
        assertEquals(expected, actual)
    }

}