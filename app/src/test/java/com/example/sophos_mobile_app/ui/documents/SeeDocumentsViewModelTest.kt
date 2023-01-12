package com.example.sophos_mobile_app.ui.documents

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sophos_mobile_app.CoroutineTestRule
import com.example.sophos_mobile_app.MainCoroutineRule
import com.example.sophos_mobile_app.data.model.Document
import com.example.sophos_mobile_app.data.repository.FakeDocumentRepository
import com.example.sophos_mobile_app.utils.ImageConverterImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SeeDocumentsViewModelTest {
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var seeDocumentViewModel: SeeDocumentsViewModel
    private lateinit var imageConverter: ImageConverterImpl


    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        val fakeDocumentRepository = FakeDocumentRepository()
        imageConverter = ImageConverterImpl()
        seeDocumentViewModel = SeeDocumentsViewModel(fakeDocumentRepository, imageConverter)

    }

    @Test
    fun getDocuments_seeDocuments_listOfUserDocuments() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            //Arrange
            val email = "alejo51120@gmail.com"
            seeDocumentViewModel.getDocuments(email)
            //Action
            val expected = listOf(
                Document(
                    "Pelaez Posada",
                    "2022-12-27T03:53:10.184Z",
                    "c34de8dc-fcf0-461d-8513-12a525505df8",
                    "Alejandro", "Incapacidad"
                ),
                Document(
                    "Pelaez Posada",
                    "2022-12-28T03:53:10.184Z",
                    "c34de8dc-fcf0-461d-aaaa--bbbb",
                    "Alejandro", "Incapacidad"
                )
            )
            val actual = seeDocumentViewModel.documents.value
            //Assert
            assertEquals(expected, actual)
        }

    @Test
    fun getDocuments_seeDocuments_emptyListBecauseWrongEmail() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            //Arrange
            val email = "alf@gmail.com"
            seeDocumentViewModel.getDocuments(email)

            //Action
            val expected = emptyList<Document>()
            val actual = seeDocumentViewModel.documents.value

            //Assert
            assertEquals(expected, actual)
        }

    @Test
    fun getImageFromDocument_itemDocumentClick_documentImageBitmap() = mainCoroutineRule.runBlockingTest{
        //Arrange
        val registerId = "28a487cf-4da3-4253-816b-58098cee1c26"
/*        PowerMockito.mockStatic(BitmapFactory::class.java)
        `when`(BitmapFactory.decodeFile(registerId)).thenReturn(bitmap)*/
        seeDocumentViewModel.getImageFromDocument(registerId)
        //seeDocumentViewModel.getImageFromDocument(registerId)
        //Action
        val actual = seeDocumentViewModel.imageBitmap.value
        //Assert
        assert(actual is Bitmap)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}