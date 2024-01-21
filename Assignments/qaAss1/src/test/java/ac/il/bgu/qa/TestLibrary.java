package ac.il.bgu.qa;

import ac.il.bgu.qa.errors.*;
import ac.il.bgu.qa.services.DatabaseService;
import ac.il.bgu.qa.services.NotificationService;
import ac.il.bgu.qa.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class TestLibrary {

    @Mock
    private DatabaseService mockDatabaseService;

    @Mock
    private ReviewService mockReviewService;

    @Mock
    private NotificationService mockNotificationService;
    @Mock
    private Book mockBook;

    @Mock
    private User mockUser;

    final String validUserId = "123456789012";
    final String validISBN = "978-0-452-28423-4";
    final String validTitle = "Title";
    final String validAuthor = "Author";

    final String validUserName = "Name";
    @InjectMocks
    private Library library;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        library = new Library(mockDatabaseService, mockReviewService);
    }
    // ------------------------------------------------------------------------ ADD BOOK --------------------------------------------------------
    /**
     * checks that:
     *                    1. the book isn't null when adding a book
     */
    @Test
    void testAddBook_whenBookIsNull_ThenReturnError() {
        // 1. Arrange
        // 1.1. Attempt to add a book with an invalid ISBN
        Book invalidBook = null;

        // 2. Action
        // 2.1. Call the method under test
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidBook));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, never()).addBook(anyString(), any(Book.class));
    }

    /**
     * @param invalidISBN - Invalid ISBN
     * checks that:
     *                    1. the ISBN isn't too short
     *                    2. the ISBN isn't too long
     *                    3. the checksum is correct
     *                    4. the ISBN value can't be null
     */
    //books
    @ParameterizedTest
    @ValueSource(strings = {"123", "12345678901234","0-340-01381-9"})
    @NullSource
    void testAddBook_whenGetInvalidISBN_ThenThrowIllegalArgumentException(String invalidISBN) {
        Book invalidBook = new Book(invalidISBN, validTitle,validAuthor);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidBook));
        verify(mockDatabaseService, never()).addBook(anyString(), any(Book.class));
    }

    /**
     * @param invalidTitle - Invalid book title
     * checks that:
     *                    1. the title is not null
     *                    2. the title is not empty
     */
    @ParameterizedTest
    @ValueSource(strings = {""})
    @NullSource
    void testAddBook_whenTitleIsInvalid_thenThrowError(String invalidTitle){
        // 1.2. Attempt to add a book with an invalid title
        Book invalidBook = new Book(validISBN, invalidTitle, validAuthor);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidBook));
        verify(mockDatabaseService, never()).addBook(anyString(), any(Book.class));
    }

    /**
     * @param invalidAuthor - Invalid Author name
     * checks that:
     *                    1. the author is not null
     *                    2. the author is not empty
     *                    3. the first letter is alphabetic
     *                    4. the last latter is alphabetic
     *                    5. consecutive special characters -> -- or \\
     *                    6. one of the characters are not "- \." (space included)
     */
    @ParameterizedTest
    @ValueSource(strings = {"","1Chen","Chen1","Chen --Frydman","a!@#$%^&*(),;a"})
    @NullSource
    void testAddBook_whenAuthorIsInvalid_ThenThrowIllegalArgumentException(String invalidAuthor){

        // 1.2. Attempt to add a book with an invalid Author Name
        Book invalidBook = new Book(validISBN, validTitle, invalidAuthor);

        // 2. Action
        // 2.1. Call the method under test
        assertThrows(IllegalArgumentException.class, () -> library.addBook(invalidBook));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, never()).addBook(anyString(), any(Book.class));
    }

    /**
     * checks that:
     *                    1. When added new book, and it already exists in the mockDB, throw an error
     */
    @Test
    void testAddBook_whenBookIsAlreadyInDB_thenThrowIllegalArgumentException(){
        when(mockBook.getISBN()).thenReturn(validISBN);
        when(mockBook.getTitle()).thenReturn(validTitle);
        when(mockBook.getAuthor()).thenReturn(validAuthor);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        verify(mockDatabaseService, never()).addBook(anyString(), any(Book.class));
    }

    @Test
    void testAddBook_whenBookIsisBorrowed_thenThrowIllegalArgumentException(){
        when(mockBook.getISBN()).thenReturn(validISBN);
        when(mockBook.getTitle()).thenReturn(validTitle);
        when(mockBook.getAuthor()).thenReturn(validAuthor);
        when(mockBook.isBorrowed()).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> library.addBook(mockBook));
        verify(mockDatabaseService, never()).addBook(anyString(), any(Book.class));
    }


    /**
     * checks that:
     *                   1. the mockDatabase gets 1 call to addBook :)
     */
    @Test
    void testAddBook_whenEverthingWorks_thenNotError() {
        when(mockBook.getISBN()).thenReturn(validISBN);
        when(mockBook.getTitle()).thenReturn(validTitle);
        when(mockBook.getAuthor()).thenReturn(validAuthor);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(null);
        library.addBook(mockBook);
        verify(mockDatabaseService, times(1)).addBook(anyString(), any(Book.class));

    }
    // ------------------------------------------------------------------------ REGISTER USER --------------------------------------------------------


    /**
     * checks that:
     *                    1. the user is not null
     */
    @Test
    void testRegisterUser_whenUserIsNull_thenThrowIllegalArgumentException(){

        // 1.2. create a user with null
        User user = null;

        // 2. Action
        // 2.1. Call the method under test
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, never()).registerUser(anyString(), any(User.class));
    }

    /**
     * @param invalidUserId - Invalid userId
     * checks that:
     *                    1. the digits userId isn't too short(<12)
     *                    2. the digits userId isn't too long(>12)
     *                    3. the userId is not null
     *                    4. the userID not is made of digits
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "1","12345678901234","12345678901234","12345678901a"})
    @NullSource
    void testRegisterUser_whenStringUserIDisInvalid_thenThrowIllegalArgumentException(String invalidUserId){
        // 1.2. create a user with invalid id
        User user = new User("name",invalidUserId,mockNotificationService);

        // 2. Action
        // 2.1. Call the method under test
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, never()).registerUser(anyString(), any(User.class));
        verify(mockNotificationService, never()).notifyUser(any(String.class), anyString());

    }

    /**
     * checks that:
     *                    1. the user's name is not null
     *                    2. the user's name is not empty
     */
    @ParameterizedTest
    @ValueSource(strings = {""})
    @NullSource
    void testRegisterUser_whenUserNameNull_ThenThrowIllegalArgumentException(String invalidUserName){
        // 1.2. create a user with invalid User name
        User user = new User(invalidUserName,validUserId,mockNotificationService);

        // 2. Action
        // 2.1. Call the method under test
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, never()).registerUser(anyString(), any(User.class));
    }

    /**
     * checks that:
     *                    1. the user is not null
     */
    @Test
    void testRegisterUser_whenNotificationServiceIsNull_thenThrowIllegalArgumentException(){

        // 1.2. create a user with invalid id
        User user = new User("name",validUserId,null);

        // 2. Action
        // 2.1. Call the method under test
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(user));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, never()).registerUser(anyString(), any(User.class));
    }

    @Test
    void testRegisterUse_whenDBgetUserByIDisNull_thenThrowIllegalArgumentException() {
        when(mockUser.getId()).thenReturn(validUserId);
        when(mockUser.getName()).thenReturn(validUserName);
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);
        assertThrows(IllegalArgumentException.class, () -> library.registerUser(mockUser));
        verify(mockDatabaseService, never()).addBook(anyString(), any(Book.class));

    }


    @Test
    void testRegisterUser_whenUserIsValid_thenThrowNoError() {
        // Test the registerUser method
        when(mockUser.getId()).thenReturn(validUserId);
        when(mockUser.getName()).thenReturn(validUserName);
        when(mockUser.getNotificationService()).thenReturn(mockNotificationService);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(null);
        assertDoesNotThrow(() -> library.registerUser(mockUser));
        // Verify that the user was registered in the databaseService
        verify(mockDatabaseService, times(1)).registerUser(mockUser.getId(), mockUser);
    }



    // ------------------------------------------------------------------------ BORROW BOOK --------------------------------------------------------
    /**
     * @param invalidISBN - Invalid ISBN
     * checks that:
     *                    1. the ISBN isn't too short
     *                    2. the ISBN isn't too long
     *                    3. the checksum is correct
     *                    4. the ISBN value can't be null
     */
    @ParameterizedTest
    @ValueSource(strings = {"123", "12345678901234","0-340-01381-9"})
    @NullSource
    void testBorrowBook_whenGetInvalidISBN_ThenReturnIllegalArgumentException(String invalidISBN) {
        // 1. Arrange

        // 1.2. Attempt to add a book with an invalid ISBN
        Book invalidBook = new Book(invalidISBN, validTitle, validAuthor);

        // 2. Action
        // 2.1. Call the method under test
        assertThrows(IllegalArgumentException.class, () -> library.borrowBook(invalidISBN,validUserId));

        // 3. Assertion
        // 3.1. Verify interactions
        verify(mockDatabaseService, never()).borrowBook(anyString(), anyString());

    }

    /**
     * Unit test for the returnBook method in the Library class.
     * Verifies the behavior when attempting to return a book that is Null.
     */
    @Test
    void testBorrowBook_whenBookIsNull_thenReturnBookNotFoundException() {
        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(null);

        // Test the borrowBook method
        assertThrows(BookNotFoundException.class,() -> library.borrowBook(validISBN,validUserId));
        verify(mockDatabaseService, never()).borrowBook(anyString(), anyString());
    }

    /**
     * @param invalidUserId - Invalid userId
     * checks that:
     *                    1. the digits userId isn't too short(<12)
     *                    2. the digits userId isn't too long(>12)
     *                    3. the userId is not null
     *                    4. the userID not is made of digits
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "1","12345678901234","12345678901234","12345678901a"})
    @NullSource
    void testBorrowBook_whenStringUserIDisInvalid_thenThrowError(String invalidUserId){
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);

        // Test the borrowBook method
        assertThrows(IllegalArgumentException.class, () -> library.borrowBook(validISBN,invalidUserId));
        verify(mockDatabaseService, never()).borrowBook(anyString(), anyString());
    }

    @Test
    void testBorrowBook_whenNoUser_ThenReturnUserNotRegisteredException() {
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(null);

        // Test the borrowBook method
        assertThrows(BookNotFoundException.class,() -> library.borrowBook(validISBN,validUserId));
        verify(mockDatabaseService, never()).borrowBook(anyString(), anyString());
    }

    /**
     * Unit test for the borrowBook method in the Library class.
     * Verifies the behavior when attempting to borrow a borrowed book it will throw an error
     */
    @Test
    void testBorrowBook_whenIsBorrowed_ThenReturnBookAlreadyBorrowedException() {
        when(mockBook.isBorrowed()).thenReturn(true);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);

        // Test the borrowBook method
        assertThrows(BookAlreadyBorrowedException.class,() -> library.borrowBook(validISBN,validUserId));
        verify(mockDatabaseService, never()).borrowBook(anyString(), anyString());
    }

    @Test
    void testBorrowBook_whenIsNull_ThenReturnBookNotFoundException() {
        when(mockBook.isBorrowed()).thenReturn(true);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(null);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);

        // Test the borrowBook method
        assertThrows(BookNotFoundException.class,() -> library.borrowBook(validISBN,validUserId));
        verify(mockDatabaseService, never()).borrowBook(anyString(), anyString());
    }
    @Test
    void testBorrowBook_whenGetUserByIdIsNull_IllegalArgumentException() {
        when(mockBook.isBorrowed()).thenReturn(true);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);

        // Test the borrowBook method
        assertThrows(BookAlreadyBorrowedException.class,() -> library.borrowBook(validISBN,validUserId));
        verify(mockDatabaseService, never()).borrowBook(anyString(), anyString());
    }

    /**
     *  Unit test for the borrowBook method in the Library class.
     *  Verifies the behavior when databaseService.getUserById returns null.
     */
    @Test
    void testBorrowBook_whenDBserviceGetUserByIdIsNull_thenThrowIllegalArgumentException() {
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(null);

        // Test the borrowBook method
        assertThrows(UserNotRegisteredException.class,() -> library.borrowBook(validISBN,validUserId));
        verify(mockDatabaseService, never()).borrowBook(anyString(), anyString());
    }
    /**
     * Unit test for the getBookByISBN method in the Library class.
     * Verifies the behavior when attempting to retrieve a book by ISBN that is already marked as borrowed.
     */
    @Test
    void testBorrowBook_whenBorrowIsSuccessful_thenReturnNotError() {
        // Mocking databaseService behavior
        Book availableBook = new Book(validISBN, validTitle,validAuthor);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(availableBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);

        // Test the borrowBook method
        assertDoesNotThrow(() -> library.borrowBook(validISBN, validUserId));

        // Verify that the book was marked as borrowed
        assertTrue(availableBook.isBorrowed());
        verify(mockDatabaseService, times(1)).borrowBook(validISBN,validUserId);
    }

    // ------------------------------------------------------------------------ RETURN BOOK --------------------------------------------------------

    /**
     * @param invalidISBN - Invalid ISBN
     * checks that:
     *                    1. the ISBN isn't too short
     *                    2. the ISBN isn't too long
     *                    3. the checksum is correct
     *                    4. the ISBN value can't be null
     */
    @ParameterizedTest
    @ValueSource(strings = {"123", "12345678901234","0-340-01381-9"})
    @NullSource
    void testReturnBook_whenGetInvalidISBN_ThenThrowIllegalArgumentException(String invalidISBN) {
        assertThrows(IllegalArgumentException.class, () -> library.returnBook(invalidISBN));
        verify(mockDatabaseService, never()).returnBook(anyString());
    }

    /**
     * Unit test for the returnBook method in the Library class.
     * Verifies the behavior when attempting to return a book that is Null.
     */
    @Test
    void testReturnBook_whenBookIsNull_thenReturn_BookNotFoundException() {
        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(null);

        // Test the borrowBook method
        assertThrows(BookNotFoundException.class,() -> library.returnBook(validISBN));
        verify(mockDatabaseService, never()).returnBook(anyString());
    }

    /**
     * Unit test for the returnBook method in the Library class.
     * Verifies the behavior when attempting to return a book that has not been borrowed.
     */
    @Test
    void testReturnBook_whenBookIsNotBorrowed_ThenThrowBookNotBorrowedException() {
        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);

        // Test the borrowBook method
        assertThrows(BookNotBorrowedException.class,() -> library.returnBook(validISBN));
        verify(mockDatabaseService, never()).returnBook(anyString());
    }


    @Test
    void testReturnBook_whenReturnIsSuccessful_thenReturnNotError() {
        when(mockBook.isBorrowed()).thenReturn(true);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);

        // Test the borrowBook method
        assertDoesNotThrow(() -> library.returnBook(validISBN));

        verify(mockDatabaseService, times(1)).returnBook(validISBN);
    }




    // ------------------------------------------------------------------------ GET BOOK BY ISBN--------------------------------------------------------

    /**
     * @param invalidISBN - Invalid ISBN
     * checks that:
     *                    1. the ISBN isn't too short
     *                    2. the ISBN isn't too long
     *                    3. the checksum is correct
     *                    4. the ISBN value can't be null
     */
    @ParameterizedTest
    @ValueSource(strings = {"123", "12345678901234","0-340-01381-9", "0-340-01382-9"})
    @NullSource
    void testGetBookByISBN_whenGetInvalidISBN_ThenThrowIllegalArgumentException(String invalidISBN){
        assertThrows(IllegalArgumentException.class, () -> library.getBookByISBN(invalidISBN,validUserId));
        verify(mockDatabaseService, never()).addBook(anyString(), any(Book.class));
    }

    /**
     * @param invalidUserId - Invalid userId
     *                      checks that:
     *                      the String userId is:
     *                      1. not empty
     *                      2. not null
     */
    @ParameterizedTest
    @ValueSource(strings = {""})
    @NullSource
    void testGetBookByISBN_whenGetUserByIdIsNull_ThenThrowIllegalArgumentException(String invalidUserId){
        assertThrows(IllegalArgumentException.class,() -> library.getBookByISBN(validISBN,invalidUserId));
        verify(mockDatabaseService, never()).getBookByISBN(anyString());
    }

    /**
     * Unit test for the getBookByISBN method in the Library class.
     * Verifies the behavior when attempting to retrieve a book by ISBN that is null.
     */
    @Test
    void testGetBookByISBN_whenBookIsNull_ThenThrowBookNotFoundException() {
        // Mocking databaseService behavior

        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(null);
        // Test the borrowBook method
        assertThrows(BookNotFoundException.class,() -> library.getBookByISBN(validISBN,validUserId));
        verify(mockDatabaseService, times(1)).getBookByISBN(anyString());
    }

    @Test
    void testGetBookByISBN_whenBookIsBorrowed_ThenThrowBookAlreadyBorrowedException() {
        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockBook.isBorrowed()).thenReturn(true);
        // Test the borrowBook method
        assertThrows(BookAlreadyBorrowedException.class,() -> library.getBookByISBN(validISBN,validUserId));
        verify(mockDatabaseService, times(1)).getBookByISBN(anyString());
    }

    /**
     * Unit test for the getBookByISBN method in the Library class.
     * Verifies the behavior when attempting to retrieve a book by ISBN that is already marked as borrowed.
     */
    @Test
    void testGetBookByISBN_whenNotifyUserWithBookReviewsIsErr_thenPrintNotificationFailed() throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
        Library spyLibrary = spy(library);

        when(mockBook.isBorrowed()).thenReturn(false);
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);

        doThrow(new NoReviewsFoundException("")).when(spyLibrary).notifyUserWithBookReviews(validISBN,validUserId);

        verify(mockNotificationService, never()).notifyUser(any(String.class), anyString());
        spyLibrary.getBookByISBN(validISBN,validUserId);

        bo.flush();
        String allWrittenLines = new String(bo.toByteArray());
        assertTrue(allWrittenLines.contains("Notification failed!"));
    }

    // ------------------------------------------------------------------------ NOTIFY USER --------------------------------------------------------

    /**
     * @param invalidISBN - Invalid ISBN
     * checks that:
     *                    1. the ISBN isn't too short
     *                    2. the ISBN isn't too long
     *                    3. the checksum is correct
     *                    4. the ISBN value can't be null
     */
    //books
    @ParameterizedTest
    @ValueSource(strings = {"123", "12345678901234","0-340-01381-9"})
    @NullSource
    void testNotifyUserWithBookReviews_whenInvalidISBN_ThenThrowIllegalArgumentException(String invalidISBN) {
        assertThrows(IllegalArgumentException.class, () -> library.notifyUserWithBookReviews(invalidISBN,validUserId));
        verify(mockNotificationService, never()).notifyUser(anyString(), anyString());
    }

    /**
     * Unit test for the notifyUserWithBookReviews method in the Library class.
     * Verifies the behavior when attempting to notify a user with book reviews,
     * but an incorrect or invalid user ID is provided, resulting in an IllegalArgumentException.
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "1","12345678901234","12345678901234","12345678901a"})
    @NullSource
    void testNotifyUserWithBookReviews_whenUserIdWrong_ThenReturnIllegalArgumentException(String invalidUserId) {
        // Test the borrowBook method
        assertThrows(IllegalArgumentException.class,() -> library.notifyUserWithBookReviews(validISBN,invalidUserId));
        verify(mockNotificationService, never()).notifyUser(anyString(), anyString());
    }


    /**
     * Unit test for the notifyUserWithBookReviews method in the Library class.
     * Verifies the behavior when attempting to notify a user with book reviews for a book that is Null.
     * Expects a BookNotFoundException to be thrown, and ensures that the NotificationService is not invoked.
     */
    @Test
    void testNotifyUserWithBookReviews_whenBookIsNull_ThenReturnBookNotFoundException() {
        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(null);

        // Test the borrowBook method
        assertThrows(BookNotFoundException.class,() -> library.notifyUserWithBookReviews(validISBN,validUserId));
        verify(mockNotificationService, never()).notifyUser(any(String.class), anyString());
    }

    /**
     * Unit test for the notifyUserWithBookReviews method in the Library class.
     * Verifies the behavior when attempting to notify a user with book reviews for a book,
     * but the user is not found in the library's user database.
     * Expects a UserNotRegisteredException to be thrown, and ensures that the NotificationService is not invoked.
     */
    @Test
    void testNotifyUserWithBookReviews_whenUserIsNull_ThenReturnUserNotRegisteredException() {
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(null);

        // Test the borrowBook method
        assertThrows(UserNotRegisteredException.class,() -> library.notifyUserWithBookReviews(validISBN,validUserId));
        verify(mockNotificationService, never()).notifyUser(any(String.class), anyString());
    }

    /**
     * Unit test for the notifyUserWithBookReviews method in the Library class.
     * Verifies the behavior when attempting to notify a user with book reviews for a book,
     * but the review service encounters an issue and throws a ReviewException.
     * Expects a ReviewServiceUnavailableException to be thrown, and ensures that the NotificationService is not invoked.
     * Also checks the it closes in "finally" block
     */
    @Test
    void testNotifyUserWithBookReviews_whenReviewServiceThrowsAndCatch_ThenReturnReviewServiceUnavailableException() {
        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(validISBN)).thenThrow(new ReviewException(""));
        // Test the borrowBook method
        assertThrows(ReviewServiceUnavailableException.class,() -> library.notifyUserWithBookReviews(validISBN,validUserId));
        verify(mockNotificationService, never()).notifyUser(any(String.class), anyString());
        verify(mockReviewService, times(1)).close();
    }

    /**
     * Unit test for the notifyUserWithBookReviews method in the Library class.
     * Verifies the behavior when attempting to notify a user with book reviews for a book,
     * but the review service returns Null.
     * Expects a NoReviewsFoundException to be thrown, and ensures that the NotificationService is not invoked.
     */
    @Test
    void notifyUserWithBookReviews_whenReviewServiceReturnsNull_ThenThrowNoReviewsFoundException() {
        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);
        when(mockReviewService.getReviewsForBook(validISBN)).thenReturn(null);

        assertThrows(NoReviewsFoundException.class,() -> library.notifyUserWithBookReviews(validISBN,validUserId));
        //verify(mockNotificationService, never()).notifyUser(validUserId, any(String.class));
        verify(mockReviewService, times(1)).getReviewsForBook(validISBN);
    }

    /**
     * Unit test for the notifyUserWithBookReviews method in the Library class.
     * Verifies the behavior when attempting to notify a user with book reviews for a book,
     * but the review service returns Empty.
     * Expects a NoReviewsFoundException to be thrown, and ensures that the NotificationService is not invoked.
     */
    @Test
    void notifyUserWithBookReviews_whenReviewServiceReturnsEmpty_ThenThrowNoReviewsFoundException() {
        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);
        List<String> emptyList = Collections.emptyList();
        when(mockReviewService.getReviewsForBook(validISBN)).thenReturn(emptyList);

        assertThrows(NoReviewsFoundException.class,() -> library.notifyUserWithBookReviews(validISBN,validUserId));
        //verify(mockNotificationService, never()).notifyUser(validUserId, anyString());
        verify(mockReviewService, times(1)).getReviewsForBook(validISBN);
    }

    /**
     * Unit test for the notifyUserWithBookReviews method in the Library class.
     * Verifies the behavior when attempting to notify a user with book reviews for a book,
     * but the notification failed
     * Expects a String to be printed to the console.
     */
    @Test
    void notifyUserWithBookReviews_whenSendNotificationThrowsNotificationException_ThenPrintNotificationFailed() throws IOException {
        //in order to copy the console
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setErr(new PrintStream(bo));

        //Library spyLibrary = spy(library);

        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);
        when(mockBook.getTitle()).thenReturn(validTitle);
        List<String> arrayList = new LinkedList<>(Arrays.asList("review1","review2"));
        String notificationMessage = "Reviews for '" + validTitle + "':\n" + String.join("\n", arrayList);
        when(mockReviewService.getReviewsForBook(validISBN)).thenReturn(arrayList);

        //User spyUser = spy(mockUser);
        doThrow(new NotificationException("")).when(mockUser).sendNotification(notificationMessage);



        assertThrows(NotificationException.class,() -> library.notifyUserWithBookReviews(validISBN,validUserId));
        //bo.flush();
        String allWrittenLines = bo.toString();
        assertTrue(allWrittenLines.contains("Notification failed! Retrying attempt 1/5"));
        assertTrue(allWrittenLines.contains("Notification failed! Retrying attempt 2/5"));
        assertTrue(allWrittenLines.contains("Notification failed! Retrying attempt 3/5"));
        assertTrue(allWrittenLines.contains("Notification failed! Retrying attempt 4/5"));
        assertTrue(allWrittenLines.contains("Notification failed! Retrying attempt 5/5"));
        verify(mockNotificationService, never()).notifyUser(validUserId, notificationMessage);
        verify(mockReviewService, times(1)).getReviewsForBook(validISBN);
    }

    /**
     * Unit test for the notifyUserWithBookReviews method in the Library class.
     * Verifies the behavior when attempting to notify a user with book reviews for a book,
     *  the notification succeeds
     * Expects a return
     */
    @Test
    void notifyUserWithBookReviews_whenSendNotificationSucceeds_ThenReturn(){

        // Mocking databaseService behavior
        when(mockDatabaseService.getBookByISBN(validISBN)).thenReturn(mockBook);
        when(mockDatabaseService.getUserById(validUserId)).thenReturn(mockUser);
        when(mockBook.getTitle()).thenReturn(validTitle);
        List<String> arrayList = new LinkedList<>(Arrays.asList("review1","review2"));
        String notificationMessage = "Reviews for '" + validTitle + "':\n" + String.join("\n", arrayList);
        when(mockReviewService.getReviewsForBook(validISBN)).thenReturn(arrayList);

        assertDoesNotThrow(() -> library.notifyUserWithBookReviews(validISBN,validUserId));
        verify(mockNotificationService, never()).notifyUser(validUserId, notificationMessage);
        verify(mockReviewService, times(1)).getReviewsForBook(validISBN);
    }

}