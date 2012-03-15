/**
 * Copyright (c) 2002-2012 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.neo4j.graphdb.factory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Setting types for Neo4j. Actual settings are in GraphDatabaseSettings
 */
public abstract class GraphDatabaseSetting
{
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public interface DefaultValue
    {
        String getDefaultValue();
    }
    
    // Implementations of GraphDatabaseSetting
    public static class BooleanSetting
        extends OptionsSetting
    {
        public BooleanSetting( String name)
        {
            super( name, TRUE, FALSE );
        }
    }
    
    public static class StringSetting
        extends GraphDatabaseSetting
    {
        private Pattern regex;
        private String formatMessage;

        public StringSetting( String name, String regex, String formatMessage )
        {
            super( name );
            this.formatMessage = formatMessage;
            this.regex = Pattern.compile( regex );
        }

        @Override
        public void validate( String value, Locale locale )
        {
            if (!regex.matcher( value ).matches())
            {
                throw new IllegalArgumentException( MessageFormat.format( formatMessage, value ) );
            }
        }
    }

    public static class IntegerSetting
        extends GraphDatabaseSetting
    {
        private String formatMessage;
        private Integer min;
        private Integer max;

        public IntegerSetting( String name, String formatMessage )
        {
            super( name );
            this.formatMessage = formatMessage;
        }

        public IntegerSetting( String name, String formatMessage, Integer min, Integer max )
        {
            super( name );
            this.formatMessage = formatMessage;
            this.min = min;
            this.max = max;
        }

        @Override
        public void validate( String value, Locale locale )
        {
            int val;
            try
            {
                val = Integer.parseInt( value );
            }
            catch( Exception e )
            {
                throw new IllegalArgumentException( MessageFormat.format( formatMessage, value ) );
            }

            // Check range
            if (min != null && val < min)
                throw new IllegalArgumentException( "Minimum allowed value is:"+min );

            if (max != null && val > max)
                throw new IllegalArgumentException( "Maximum allowed value is:"+max );
        }
    }

    public static class LongSetting
        extends GraphDatabaseSetting
    {
        private String formatMessage;
        private Long min;
        private Long max;

        public LongSetting( String name, String formatMessage )
        {
            super( name );
            this.formatMessage = formatMessage;
        }

        public LongSetting( String name, String formatMessage, Long min, Long max )
        {
            super( name );
            this.formatMessage = formatMessage;
            this.min = min;
            this.max = max;
        }

        @Override
        public void validate( String value, Locale locale )
        {
            long val;
            try
            {
                val = Long.parseLong( value );
            }
            catch( Exception e )
            {
                throw new IllegalArgumentException( MessageFormat.format( formatMessage, value ) );
            }

            // Check range
            if (min != null && val < min)
                throw new IllegalArgumentException( "Minimum allowed value is:"+min );

            if (max != null && val > max)
                throw new IllegalArgumentException( "Maximum allowed value is:"+max );
        }
    }

    public static class FloatSetting
        extends GraphDatabaseSetting
    {
        private String formatMessage;
        private Float min;
        private Float max;

        public FloatSetting( String name, String formatMessage )
        {
            super( name );
            this.formatMessage = formatMessage;
        }

        public FloatSetting( String name, String formatMessage, Float min, Float max )
        {
            super( name );
            this.formatMessage = formatMessage;
            this.min = min;
            this.max = max;
        }

        @Override
        public void validate( String value, Locale locale )
        {
            float val;
            try
            {
                val = Float.parseFloat( value );
            }
            catch( Exception e )
            {
                throw new IllegalArgumentException( MessageFormat.format( formatMessage, value ) );
            }

            // Check range
            if (min != null && val < min)
                throw new IllegalArgumentException( "Minimum allowed value is:"+min );

            if (max != null && val > max)
                throw new IllegalArgumentException( "Maximum allowed value is:"+max );
        }
    }

    public static class DoubleSetting
        extends GraphDatabaseSetting
    {
        private String formatMessage;
        private Double min;
        private Double max;

        public DoubleSetting( String name, String formatMessage )
        {
            super( name );
            this.formatMessage = formatMessage;
        }

        public DoubleSetting( String name, String formatMessage, Double min, Double max )
        {
            super( name );
            this.formatMessage = formatMessage;
            this.min = min;
            this.max = max;
        }

        @Override
        public void validate( String value, Locale locale )
        {
            double val;
            try
            {
                val = Double.parseDouble( value );
            }
            catch( Exception e )
            {
                throw new IllegalArgumentException( MessageFormat.format( formatMessage, value ) );
            }

            // Check range
            if (min != null && val < min)
                throw new IllegalArgumentException( "Minimum allowed value is:"+min );

            if (max != null && val > max)
                throw new IllegalArgumentException( "Maximum allowed value is:"+max );
        }
    }

    public static class OptionsSetting
        extends GraphDatabaseSetting
    {
        String[] options;
        
        protected OptionsSetting( String name, String... options)
        {
            super( name );
            
            this.options  = options;
        }

        @Override
        public void validate( String value, Locale locale )
            throws IllegalArgumentException
        {
            for( String option : options() )
            {
                if (value.equalsIgnoreCase( option ))
                    return;
            }

            throw new IllegalArgumentException( "Value '"+value+"' is not valid. Valid options are:"+ Arrays.asList( options() ) );
        }


        public String[] options()
        {
            return options;
        }
    }

    // Specialized settings
    public static class CacheTypeSetting
        extends OptionsSetting
    {
        public static final String weak = "weak";
        public static final String soft = "soft";
        public static final String old = "old";
        public static final String none = "none";
        public static final String strong = "strong";

        public CacheTypeSetting( )
        {
            super( "cache_type", weak, soft, old, none, strong);
        }
    }

    public static class CypherParserSetting
        extends OptionsSetting
    {
        public static final String v1_5 = "1.5";
        public static final String v1_6 = "1.6";

        public CypherParserSetting( )
        {
            super( "cypher_parser_version", v1_5, v1_6);
        }
    }

    public static class UseMemoryMappedBuffers
        extends BooleanSetting
        implements DefaultValue
    {
        public UseMemoryMappedBuffers( )
        {
            super( "use_memory_mapped_buffers" );
        }

        @Override
        public String getDefaultValue()
        {
            // if on windows, default no memory mapping
            if ( osIsWindows() )
            {
                return FALSE;
            }
            else
            {
                // If not on win, default use memory mapping
                return TRUE;
            }
        }
    }

    private String name;

    protected GraphDatabaseSetting( String name )
    {
        this.name = name;
    }

    public String name()
    {
        return name;
    }
    
    public void validate(String value)
        throws IllegalArgumentException
    {
        validate( value, Locale.getDefault() );
    }
    
    public abstract void validate(String value, Locale locale);

    public static boolean osIsWindows()
    {
        String nameOs = System.getProperty( "os.name" );
        return nameOs.startsWith("Windows");
    }

    public static boolean osIsMacOS()
    {
        String nameOs = System.getProperty( "os.name" );
        return nameOs.equalsIgnoreCase( "Mac OS X" );
    }
}