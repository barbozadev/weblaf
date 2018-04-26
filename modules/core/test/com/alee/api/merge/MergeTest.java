/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.api.merge;

import com.alee.api.jdk.Objects;
import com.alee.api.matcher.EqualMatcher;
import com.alee.api.merge.behavior.BasicMergeBehavior;
import com.alee.api.merge.behavior.ListMergeBehavior;
import com.alee.api.merge.behavior.PreserveOnMerge;
import com.alee.api.merge.behavior.ReflectionMergeBehavior;
import com.alee.api.merge.nullresolver.OverwritingNullResolver;
import com.alee.api.merge.nullresolver.SkippingNullResolver;
import com.alee.api.merge.unknownresolver.ExceptionUnknownResolver;
import com.alee.utils.ArrayUtils;
import com.alee.utils.CollectionUtils;
import com.alee.utils.reflection.ModifierType;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.List;

/**
 * Set of JUnit tests for {@link Merge}.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public final class MergeTest
{
    /**
     * Testing custom merge configuration on basic objects.
     */
    @Test
    public void customBasicMerge ()
    {
        final Merge merge = new Merge (
                new SkippingNullResolver (),
                new ExceptionUnknownResolver (),
                new BasicMergeBehavior ()
        );

        checkMergeResult ( merge.merge ( null, null ), null );
        checkMergeResult ( merge.merge ( 1, null ), 1 );
        checkMergeResult ( merge.merge ( null, 2 ), 2 );
        checkMergeException ( merge, 1, "text", MergeException.class );
        checkMergeResult ( merge.merge ( 1, 2 ), 2 );
        checkMergeResult ( merge.merge ( false, true ), true );
        checkMergeResult ( merge.merge ( true, false ), false );
        checkMergeResult ( merge.merge ( TestObject.class, ParentTestObject.class ), ParentTestObject.class );
        checkMergeResult ( merge.merge ( "test", "text" ), "text" );
        checkMergeResult ( merge.merge ( new Date (), new Date ( 0 ) ), new Date ( 0 ) );
        checkMergeResult ( merge.merge ( Color.RED, Color.BLACK ), new Color ( 0, 0, 0 ) );
        checkMergeResult (
                merge.merge (
                        new Font ( "Arial", Font.PLAIN, 13 ),
                        new Font ( "Tahoma", Font.BOLD, 12 )
                ),
                new Font ( "Tahoma", Font.BOLD, 12 )
        );
        checkMergeResult (
                merge.merge (
                        new Insets ( -1, 0, 1, 2 ),
                        new Insets ( 3, 3, 3, 3 )
                ),
                new Insets ( 3, 3, 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Dimension ( -1, 0 ),
                        new Dimension ( 3, 3 )
                ),
                new Dimension ( 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Point ( -1, 0 ),
                        new Point ( 3, 3 )
                ),
                new Point ( 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Point2D.Float ( -1.0f, 0.0f ),
                        new Point2D.Float ( 3.0f, 3.0f )
                ),
                new Point2D.Float ( 3.0f, 3.0f )
        );
        checkMergeResult (
                merge.merge (
                        new Point2D.Double ( -1.0d, 0.0d ),
                        new Point2D.Double ( 3.0d, 3.0d )
                ),
                new Point2D.Double ( 3.0d, 3.0d )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle ( -1, 0, 1, 2 ),
                        new Rectangle ( 3, 3, 3, 3 )
                ),
                new Rectangle ( 3, 3, 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle2D.Float ( -1.0f, 0.0f, 1.0f, 2.0f ),
                        new Rectangle2D.Float ( 3.0f, 3.0f, 3.0f, 3.0f )
                ),
                new Rectangle2D.Float ( 3.0f, 3.0f, 3.0f, 3.0f )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle2D.Double ( -1.0d, 0.0d, 1.0d, 2.0d ),
                        new Rectangle2D.Double ( 3.0d, 3.0d, 3.0d, 3.0d )
                ),
                new Rectangle2D.Double ( 3.0d, 3.0d, 3.0d, 3.0d )
        );
    }

    /**
     * Testing custom merge configuration with nulls skipping, only exact types merging and one basic merge behavior.
     */
    @Test
    public void skippingBasicMerge ()
    {
        final Merge merge = new Merge (
                new SkippingNullResolver (),
                new ExceptionUnknownResolver (),
                new BasicMergeBehavior ()
        );

        checkMergeResult ( merge.merge ( null, null ), null );
        checkMergeResult ( merge.merge ( 1, null ), 1 );
        checkMergeResult ( merge.merge ( null, 2 ), 2 );
        checkMergeException ( merge, 1, "text", MergeException.class );
        checkMergeResult ( merge.merge ( 1, 2 ), 2 );
        checkMergeResult ( merge.merge ( false, true ), true );
        checkMergeResult ( merge.merge ( true, false ), false );
        checkMergeResult ( merge.merge ( TestObject.class, ParentTestObject.class ), ParentTestObject.class );
        checkMergeResult ( merge.merge ( "test", "text" ), "text" );
        checkMergeResult ( merge.merge ( new Date (), new Date ( 0 ) ), new Date ( 0 ) );
        checkMergeResult ( merge.merge ( Color.RED, Color.BLACK ), new Color ( 0, 0, 0 ) );
        checkMergeResult (
                merge.merge (
                        new Font ( "Arial", Font.PLAIN, 13 ),
                        new Font ( "Tahoma", Font.BOLD, 12 )
                ),
                new Font ( "Tahoma", Font.BOLD, 12 )
        );
        checkMergeResult (
                merge.merge (
                        new Insets ( -1, 0, 1, 2 ),
                        new Insets ( 3, 3, 3, 3 )
                ),
                new Insets ( 3, 3, 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Dimension ( -1, 0 ),
                        new Dimension ( 3, 3 )
                ),
                new Dimension ( 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Point ( -1, 0 ),
                        new Point ( 3, 3 )
                ),
                new Point ( 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Point2D.Float ( -1.0f, 0.0f ),
                        new Point2D.Float ( 3.0f, 3.0f )
                ),
                new Point2D.Float ( 3.0f, 3.0f )
        );
        checkMergeResult (
                merge.merge (
                        new Point2D.Double ( -1.0d, 0.0d ),
                        new Point2D.Double ( 3.0d, 3.0d )
                ),
                new Point2D.Double ( 3.0d, 3.0d )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle ( -1, 0, 1, 2 ),
                        new Rectangle ( 3, 3, 3, 3 )
                ),
                new Rectangle ( 3, 3, 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle2D.Float ( -1.0f, 0.0f, 1.0f, 2.0f ),
                        new Rectangle2D.Float ( 3.0f, 3.0f, 3.0f, 3.0f )
                ),
                new Rectangle2D.Float ( 3.0f, 3.0f, 3.0f, 3.0f )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle2D.Double ( -1.0d, 0.0d, 1.0d, 2.0d ),
                        new Rectangle2D.Double ( 3.0d, 3.0d, 3.0d, 3.0d )
                ),
                new Rectangle2D.Double ( 3.0d, 3.0d, 3.0d, 3.0d )
        );
    }

    /**
     * Testing custom merge configuration with nulls overwriting, only exact types merging and one basic merge behavior.
     */
    @Test
    public void overwritingBasicMerge ()
    {
        final Merge merge = new Merge (
                new OverwritingNullResolver (),
                new ExceptionUnknownResolver (),
                new BasicMergeBehavior ()
        );

        checkMergeResult ( merge.merge ( null, null ), null );
        checkMergeResult ( merge.merge ( 1, null ), null );
        checkMergeResult ( merge.merge ( null, 2 ), 2 );
        checkMergeException ( merge, 1, "text", MergeException.class );
        checkMergeResult ( merge.merge ( 1, 2 ), 2 );
        checkMergeResult ( merge.merge ( false, true ), true );
        checkMergeResult ( merge.merge ( true, false ), false );
        checkMergeResult ( merge.merge ( TestObject.class, ParentTestObject.class ), ParentTestObject.class );
        checkMergeResult ( merge.merge ( "test", "text" ), "text" );
        checkMergeResult ( merge.merge ( new Date (), new Date ( 0 ) ), new Date ( 0 ) );
        checkMergeResult ( merge.merge ( Color.RED, Color.BLACK ), new Color ( 0, 0, 0 ) );
        checkMergeResult (
                merge.merge (
                        new Font ( "Arial", Font.PLAIN, 13 ),
                        new Font ( "Tahoma", Font.BOLD, 12 )
                ),
                new Font ( "Tahoma", Font.BOLD, 12 )
        );
        checkMergeResult (
                merge.merge (
                        new Insets ( -1, 0, 1, 2 ),
                        new Insets ( 3, 3, 3, 3 )
                ),
                new Insets ( 3, 3, 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Dimension ( -1, 0 ),
                        new Dimension ( 3, 3 )
                ),
                new Dimension ( 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Point ( -1, 0 ),
                        new Point ( 3, 3 )
                ),
                new Point ( 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Point2D.Float ( -1.0f, 0.0f ),
                        new Point2D.Float ( 3.0f, 3.0f )
                ),
                new Point2D.Float ( 3.0f, 3.0f )
        );
        checkMergeResult (
                merge.merge (
                        new Point2D.Double ( -1.0d, 0.0d ),
                        new Point2D.Double ( 3.0d, 3.0d )
                ),
                new Point2D.Double ( 3.0d, 3.0d )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle ( -1, 0, 1, 2 ),
                        new Rectangle ( 3, 3, 3, 3 )
                ),
                new Rectangle ( 3, 3, 3, 3 )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle2D.Float ( -1.0f, 0.0f, 1.0f, 2.0f ),
                        new Rectangle2D.Float ( 3.0f, 3.0f, 3.0f, 3.0f )
                ),
                new Rectangle2D.Float ( 3.0f, 3.0f, 3.0f, 3.0f )
        );
        checkMergeResult (
                merge.merge (
                        new Rectangle2D.Double ( -1.0d, 0.0d, 1.0d, 2.0d ),
                        new Rectangle2D.Double ( 3.0d, 3.0d, 3.0d, 3.0d )
                ),
                new Rectangle2D.Double ( 3.0d, 3.0d, 3.0d, 3.0d )
        );
    }

    /**
     * Testing {@link Merge#basicRaw()} configuration on custom objects.
     * These tests are important as outcome of predefined configurations shouldn't normally be changed.
     */
    @Test
    public void basicObjectMerge ()
    {
        final Merge merge = Merge.basicRaw ();

        checkMergeResult (
                merge.merge (
                        new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                        null
                ),
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
        );
        checkMergeResult (
                merge.merge (
                        null,
                        new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
                ),
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
        );
        checkMergeException (
                merge,
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                "text",
                MergeException.class
        );
        checkMergeException (
                merge,
                "text",
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                MergeException.class
        );
        checkMergeException (
                merge,
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                new TestObject ( false, null, 2, CollectionUtils.asList ( "2", "3" ) ),
                MergeException.class
        );
    }

    /**
     * Testing {@link Merge#deepRaw()} configuration on custom objects.
     * These tests are important as outcome of predefined configurations shouldn't normally be changed.
     */
    @Test
    public void deepObjectMerge ()
    {
        final Merge merge = Merge.deepRaw ();

        checkMergeResult (
                merge.merge (
                        new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                        null
                ),
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
        );
        checkMergeResult (
                merge.merge (
                        null,
                        new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
                ),
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
        );
        checkMergeException (
                merge,
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                "text",
                MergeException.class
        );
        checkMergeException (
                merge,
                "text",
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                MergeException.class
        );
        checkMergeResult (
                merge.merge (
                        new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                        new TestObject ( false, null, 2, CollectionUtils.asList ( "2", "3" ) )
                ),
                new TestObject ( true, "text", 2, CollectionUtils.asList ( "1", "2", "2", "3" ) )
        );
    }

    /**
     * Testing custom merge configuration with nulls skipping, only exact types merging and multiple merge behaviors.
     */
    @Test
    public void customObjectMerge ()
    {
        final Merge merge = new Merge (
                new SkippingNullResolver (),
                new ExceptionUnknownResolver (),
                new BasicMergeBehavior (),
                new ListMergeBehavior ( new EqualMatcher () ),
                new ReflectionMergeBehavior ( ReflectionMergeBehavior.Policy.mergeable, ModifierType.STATIC )
        );

        checkMergeResult (
                merge.merge (
                        new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                        null
                ),
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
        );
        checkMergeResult (
                merge.merge (
                        null,
                        new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
                ),
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) )
        );
        checkMergeException (
                merge,
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                "text",
                MergeException.class
        );
        checkMergeException (
                merge,
                "text",
                new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                MergeException.class
        );
        checkMergeResult (
                merge.merge (
                        new TestObject ( true, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                        new TestObject ( false, null, 2, CollectionUtils.asList ( "2", "3" ) )
                ),
                new TestObject ( true, "text", 2, CollectionUtils.asList ( "1", "2", "3" ) )
        );
    }

    /**
     * Testing custom merge configuration with nulls skipping, only exact types merging and multiple merge behaviors.
     */
    @Test
    public void customDifferentObjectMerge ()
    {
        final Merge merge = new Merge (
                new SkippingNullResolver (),
                new ExceptionUnknownResolver (),
                new BasicMergeBehavior (),
                new ListMergeBehavior ( new EqualMatcher () ),
                new ReflectionMergeBehavior ( ReflectionMergeBehavior.Policy.mergeable, ModifierType.STATIC )
        );

        /**
         * This merge operation must simply return the merged object.
         */
        checkMergeResult (
                merge.merge (
                        new TestObject ( false, "text", 1, CollectionUtils.asList ( "1", "2" ) ),
                        new ParentTestObject ( true, null, 2, CollectionUtils.asList ( "2", "3" ), new Object[]{ "123", 1 } )
                ),
                new ParentTestObject ( true, null, 2, CollectionUtils.asList ( "2", "3" ), new Object[]{ "123", 1 } )
        );

        /**
         * This merge operation must merge all data from {@link TestObject} on top of {@link ParentTestObject} data.
         */
        checkMergeResult (
                merge.merge (
                        new ParentTestObject ( true, "text", 1, CollectionUtils.asList ( "2", "3" ), new Object[]{ "123", 1 } ),
                        new TestObject ( false, null, 2, CollectionUtils.asList ( "1", "2" ) )
                ),
                new ParentTestObject ( true, "text", 2, CollectionUtils.asList ( "2", "3", "1" ), new Object[]{ "123", 1 } )
        );
    }

    /**
     * Asserts merge result.
     *
     * @param result   merge result
     * @param expected expected result
     */
    private void checkMergeResult ( final Object result, final Object expected )
    {
        if ( Objects.notEquals ( result, expected ) )
        {
            throw new MergeException ( String.format (
                    "Unexpected merge result: %s" + "\n" + "Expected result: %s",
                    result, expected
            ) );
        }
    }

    /**
     * Asserts merge result.
     *
     * @param merge     {@link Merge}
     * @param base      base object
     * @param merged    merged object
     * @param exception expected {@link Exception} class
     */
    private void checkMergeException ( final Merge merge, final Object base, final Object merged,
                                       final Class<? extends Exception> exception )
    {
        try
        {
            final Object result = merge.merge ( base, merged );
            throw new MergeException ( String.format (
                    "Merge succeeded when it shouldn't: %s with %s" + "\n" + "Resulting object is: %s",
                    base, merged, result
            ) );
        }
        catch ( final Exception e )
        {
            if ( Objects.notEquals ( e.getClass (), exception ) )
            {
                throw new MergeException ( "Unknown merge exception", e );
            }
        }
    }

    /**
     * Sample object for merging.
     */
    public static class TestObject implements Mergeable, Cloneable
    {
        /**
         * Sample {@link boolean} data.
         */
        @PreserveOnMerge
        private final boolean bool;

        /**
         * Sample {@link String} data.
         */
        protected final String text;

        /**
         * Sample {@link int} data.
         */
        final int number;

        /**
         * Sample {@link List} data.
         */
        public transient final List<String> list;

        /**
         * Constructs new {@link TestObject}.
         *
         * @param bool   sample {@link boolean} data
         * @param text   sample {@link String} data
         * @param number sample {@link int} data
         * @param list   sample {@link List} data
         */
        public TestObject ( final boolean bool, final String text, final int number, final List<String> list )
        {
            this.bool = bool;
            this.text = text;
            this.number = number;
            this.list = list;
        }

        /**
         * Overridden to properly compare all data within {@link MergeTest#checkMergeResult(Object, Object)}.
         */
        @Override
        public boolean equals ( final Object object )
        {
            return object instanceof TestObject &&
                    bool == ( ( TestObject ) object ).bool &&
                    Objects.equals ( text, ( ( TestObject ) object ).text ) &&
                    number == ( ( TestObject ) object ).number &&
                    CollectionUtils.equals ( list, ( ( TestObject ) object ).list, true );
        }

        @Override
        public String toString ()
        {
            return getClass ().getSimpleName () + "{" + "bool=" + bool + ", " + "text='" + text + "', " +
                    "number=" + number + ", " + "list=" + list + "}";
        }
    }

    /**
     * Another sample object for merging.
     */
    public static class ParentTestObject extends TestObject
    {
        /**
         * Sample array data.
         */
        protected final Object[] data;

        /**
         * Constructs new {@link TestObject}.
         *
         * @param bool   sample {@link boolean} data
         * @param text   sample {@link String} data
         * @param number sample {@link int} data
         * @param list   sample {@link List} data
         * @param data   sample array data
         */
        public ParentTestObject ( final boolean bool, final String text, final int number, final List<String> list, final Object[] data )
        {
            super ( bool, text, number, list );
            this.data = data;
        }

        /**
         * Overridden to properly compare all data within {@link MergeTest#checkMergeResult(Object, Object)}.
         */
        @Override
        public boolean equals ( final Object object )
        {
            return object instanceof ParentTestObject &&
                    super.equals ( object ) &&
                    ArrayUtils.equals ( data, ( ( ParentTestObject ) object ).data );
        }
    }
}