package test;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import sheep.SheepException;
import sheep.array.ArrayRunner;

@RunWith(Enclosed.class)
public class SheepTest {
    private static final String testPath = "src/test/sheep";
    public static class StandardOut {
        private ByteArrayOutputStream baos;
        private PrintStream out;
        @Before
        public void setUp() {
            this.baos = new ByteArrayOutputStream();
            this.out = System.out;
            System.setOut(new PrintStream(new BufferedOutputStream(this.baos)));
        }

        @After
        public void tearDown() {
            System.setOut(this.out);
        }

        protected String makeString(String[] arg) {
            return String.join("\n", arg) + "\n";
        }

        @Test
        public void testAry() throws Throwable {
            String fileName = SheepTest.testPath + "/ary_test.sheep";
            String[] expected = { "2", "hoge", "bar", "ter" };
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testFor() throws Throwable {
            String fileName = SheepTest.testPath + "/for_test.sheep";
            String[] expected = { "0", "1", "2", "0", "1", "1", "1", "10" };
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testConst() throws Throwable {
            String fileName = SheepTest.testPath + "/const/const_test.sheep";
            String[] expected = { "20", "2", "11" };
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testWhile() throws Throwable {
            String fileName = SheepTest.testPath + "/while_test.sheep";
            String[] expected = { "0", "1", "2", "2" };
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testIf() throws Throwable {
            String fileName = SheepTest.testPath + "/if/if_test.sheep";
            String[] expected = { "7", "5", "10", "99" };
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testClosure() throws Throwable {
            String fileName = SheepTest.testPath + "/closure_test.sheep";
            String[] expected = { "5", "3", "8" };
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testClass() throws Throwable {
            String fileName = SheepTest.testPath + "/class_test.sheep";
            String[] expected = { "21", "18", "-7" };
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testElseif() throws Throwable {
            String fileName = SheepTest.testPath + "/if/elif_test.sheep";
            String[] expected = {"a>5", "a>3", "else", "a>0"};
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testAndOr() throws Throwable {
            String fileName = SheepTest.testPath + "/and_or_test.sheep";
            String[] expected = {"false", "true", "true", "true"};
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testAssign() throws Throwable {
            String fileName = SheepTest.testPath + "/assign.sheep";
            String[] expected = {"1", "3", "5", "2", "0", "1", "2"};
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }

        @Test
        public void testOperator() throws Throwable {
            String fileName = SheepTest.testPath + "/operator_test.sheep";
            String[] expected = {"true", "false"};
            ArrayRunner.run(new String[] { fileName });
            System.out.flush();
            String result = this.baos.toString();
            assertEquals(makeString(expected), result);
        }
    }

    public static class RaiseExceptionOfSheep {
        @Test
        public void testConstAssignException() {
            String fileName = SheepTest.testPath + "/const/assign_exception.sheep";
            try {
                ArrayRunner.run(new String[] { fileName });
                fail();
            } catch(Throwable e) {
                assertEquals("cannot assign to a constant at line 2", e.getMessage());
            }
        }

        @Test
        public void testConstWithoutInitializeException() {
            String fileName = SheepTest.testPath + "/const/noinit_exception.sheep";
            try {
                ArrayRunner.run(new String[] { fileName });
                fail();
            } catch (Throwable e) {
                assertEquals("const variable must have an initializer at line 2", e.getMessage());
            }
        }
    }
}
