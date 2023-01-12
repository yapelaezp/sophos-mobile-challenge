package com.example.sophos_mobile_app.ui.documents

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sophos_mobile_app.MainCoroutineRule
import com.example.sophos_mobile_app.data.api.ResponseStatus
import com.example.sophos_mobile_app.data.repository.FakeDocumentRepository
import com.example.sophos_mobile_app.data.repository.FakeOfficeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SendDocumentsViewModelTest{
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var sendDocumentViewModel: SendDocumentsViewModel

    @Before
    fun setup() {
        val fakeDocumentRepository = FakeDocumentRepository()
        val fakeOfficeRepository = FakeOfficeRepository()
        sendDocumentViewModel = SendDocumentsViewModel(fakeDocumentRepository, fakeOfficeRepository)
    }

    @Test
    fun createNewDocument_sendData_documentCreatedTrue() = mainCoroutineRule.runBlockingTest {
        //Arrange
        val idType = "CC"
        val identification = "1234"
        val name = "Alejandro"
        val lastname = "Paláez"
        val city = "Medellín"
        val email = "alejo51120@gmail.com"
        val attachedType = "Permiso"
        val attached = "a%wklsi9a&%2KJkjakj"
        sendDocumentViewModel.createNewDocument(idType, identification, name, lastname, city, email, attachedType, attached)

        //Action
        val actual = sendDocumentViewModel.statusPost.value

        //Assert
        assert(actual is ResponseStatus.Success)
    }

    @Test
    fun getOffices_loadCities_listOfCities() = mainCoroutineRule.runBlockingTest {
        //Arrange
        sendDocumentViewModel.getOffices()

        //Action
        val actual = sendDocumentViewModel.cities.value
        val expected = setOf("Chile","Estados Unidos","Bogotá","Medellín","Panamá", "México")

        //Assert
        assertEquals(expected, actual)
    }
}