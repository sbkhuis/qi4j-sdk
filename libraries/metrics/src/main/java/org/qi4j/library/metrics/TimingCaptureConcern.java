/*
 * Copyright (c) 2012, Niclas Hedhman. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *     You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.qi4j.library.metrics;

import java.lang.reflect.Method;
import org.qi4j.api.common.AppliesTo;
import org.qi4j.api.common.Optional;
import org.qi4j.api.injection.scope.Invocation;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.metrics.MetricsProvider;

@AppliesTo( TimingCapture.class )
public class TimingCaptureConcern extends TimingCaptureAllConcern
{

    public TimingCaptureConcern( @Service @Optional MetricsProvider metrics,
                                 @Invocation Method method
    )
    {
        super( metrics, method );
    }

    @Override
    public Object invoke( Object proxy, Method method, Object[] args )
        throws Throwable
    {
        return super.invoke( proxy, method, args );
    }
}
