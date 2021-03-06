/*
 *  This file is part of SMG, a symbolic memory graph Java library
 *  Originally developed as part of CPAChecker, the configurable software verification platform
 *
 *  Copyright (C) 2011-2015  Petr Muller
 *  Copyright (C) 2007-2014  Dirk Beyer
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package cz.afri.smg.objects;

import java.util.Set;

import com.google.common.collect.Sets;

import cz.afri.smg.abstraction.SMGConcretisation;
import cz.afri.smg.graphs.ReadableSMG;

public abstract class SMGAbstractObject extends SMGObject {

  protected SMGAbstractObject(final int pSize, final String pLabel) {
    super(pSize, pLabel);
  }

  protected SMGAbstractObject(final SMGObject pPrototype) {
    super(pPrototype);
  }

  @Override
  public final boolean isAbstract() {
    return true;
  }

  public abstract boolean matchGenericShape(SMGAbstractObject pOther);

  public abstract boolean matchSpecificShape(SMGAbstractObject pOther);

  public final Set<ReadableSMG> concretise(final ReadableSMG pSmg) {
    SMGConcretisation concretisation = createConcretisation();
    if (concretisation == null) {
      return Sets.newHashSet(pSmg);
    }
    return concretisation.execute(pSmg);
  }

  protected abstract SMGConcretisation createConcretisation();
}
