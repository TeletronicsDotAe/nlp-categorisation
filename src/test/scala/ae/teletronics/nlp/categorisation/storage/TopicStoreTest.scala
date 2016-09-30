package ae.teletronics.nlp.categorisation.storage

import java.nio.file.{Files, Paths}
import java.util

import ae.teletronics.nlp.categorisation.Category
import org.junit.Assert._
import org.junit.{After, Test}

/**
  * Created by trym on 26-09-2016.
  */
class TopicStoreTest {

  private val storageFile = "target/storage-test.db"
  private val underTest = new TopicStore(storageFile)
  private val key = "s"

  @After
  def afterTest(): Unit = {
    Files.deleteIfExists(Paths.get(storageFile))
  }

  @Test
  def testCreate(): Unit = {
    assertNull(underTest.create(key))
    // return old value if overriding
    assertNotNull(underTest.create(key))
  }

  @Test
  def testGet(): Unit = {
    // start state
    underTest.create(key)
    // action
    assertNotNull(underTest.get(key))
    assertEquals(key, underTest.get(key).name)
    assertEquals(0, underTest.get(key).entries.size())
  }

  @Test
  def testList(): Unit = {
    // start state
    underTest.create(key)
    // action
    assertNotNull(underTest.list())
    assertEquals(1, underTest.list().size)
  }

  @Test
  def testUpdate(): Unit = {
    // start state
    underTest.create(key)
    assertNotNull(underTest.get(key))
    val t = new Category(key, util.Arrays.asList("cat1"))
    // action
    assertNotNull(underTest.update(t))
  }

  @Test
  def testDelete(): Unit = {
    // start state
    underTest.create(key)
    // action
    assertNotNull(underTest.delete(key))
    // verify
    assertNotNull(underTest.list())
    assertEquals(0, underTest.list().size)
  }

}
