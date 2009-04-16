/* Copyright 2008 Neo Technology, http://neotechnology.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.entitystore.neo4j.state;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.RelationshipType;
import org.qi4j.api.common.QualifiedName;
import org.qi4j.api.entity.association.ManyAssociation;
import org.qi4j.entitystore.neo4j.NeoIdentityIndex;
import org.qi4j.spi.entity.QualifiedIdentity;
import org.qi4j.spi.entity.association.AssociationDescriptor;
import org.qi4j.spi.entity.association.ManyAssociationType;

/**
 * @author Tobias Ivarsson (tobias.ivarsson@neotechnology.com)
 */
class ManyAssociationFactory
{
    private static final Map<QualifiedName, ManyAssociationFactory> cache = new HashMap<QualifiedName, ManyAssociationFactory>();


    static ManyAssociationFactory getFactory( ManyAssociationType model )
    {
        QualifiedName qName = model.qualifiedName();
        ManyAssociationFactory value = cache.get( qName );
        if( value == null )
        {
            synchronized( cache )
            {
                value = cache.get( qName );
                if( value == null )
                {
                    cache.put( qName, value = new ManyAssociationFactory( model ) );
                }
            }
        }
        return value;
    }

    public static ManyAssociationFactory load( QualifiedName qName, String typeString )
    {
        ManyAssociationFactory value = cache.get( qName );
        if( value == null )
        {
            synchronized( cache )
            {
                value = cache.get( qName );
                if( value == null )
                {
                    cache.put( qName, value = new ManyAssociationFactory( qName, typeString ) );
                }
            }
        }
        return value;
    }

    private final QualifiedName qName;
    private final CollectionFactory factory;

    private ManyAssociationFactory( ManyAssociationType model )
    {
        this.qName = model.qualifiedName();
        this.factory = CollectionFactory.getFactoryFor( model );
    }

    public ManyAssociationFactory( QualifiedName qName, String typeString )
    {
        this.qName = qName;
        this.factory = CollectionFactory.getFactoryFor( typeString );
    }

    QualifiedName getQualifiedName()
    {
        return qName;
    }

    IndirectCollection createPreloadedCollection( Collection<QualifiedIdentity> manyAssociation )
    {
        return factory.createPreloadedCollection( manyAssociation );
    }

    Collection<QualifiedIdentity> createNodeCollection( DirectEntityState state, NeoService neo, NeoIdentityIndex idIndex )
    {
        return factory.createNodeCollection( this, state, neo, idIndex );
    }

    static boolean isManyAssociation( AssociationDescriptor model )
    {
        return ManyAssociation.class.isAssignableFrom( model.accessor().getReturnType() );
    }

    RelationshipType createAssociationType( LinkType type )
    {
        return type.getRelationshipType( qName.name() );
    }

    public String typeString()
    {
        return factory.typeString();
    }
}
