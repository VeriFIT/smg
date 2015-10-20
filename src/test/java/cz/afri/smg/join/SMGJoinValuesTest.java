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
package cz.afri.smg.join;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.afri.smg.graphs.SMGEdgePointsTo;
import cz.afri.smg.graphs.SMGFactory;
import cz.afri.smg.graphs.SMGValueFactory;
import cz.afri.smg.graphs.WritableSMG;
import cz.afri.smg.objects.SMGRegion;

public class SMGJoinValuesTest {
  private static final int SIZE8 = 8;
	private WritableSMG smg1;
  private WritableSMG smg2;
  private WritableSMG smgDest;

  private SMGNodeMapping mapping1;
  private SMGNodeMapping mapping2;

  private final Integer value1 = SMGValueFactory.getNewValue();
  private final Integer value2 = SMGValueFactory.getNewValue();
  private final Integer value3 = SMGValueFactory.getNewValue();

  @Before
	public final void setUp() {
    smg1 = SMGFactory.createWritableSMG();
    smg2 = SMGFactory.createWritableSMG();
    smgDest = SMGFactory.createWritableSMG();

    mapping1 = new SMGNodeMapping();
    mapping2 = new SMGNodeMapping();
  }

//  Test disabled until Join is not called correctly from isLessOrEqual (see SMGJoinValues)
//  @Test
//  public void joinValuesIdenticalTest() throws SMGInconsistentException {
//    smg1.addValue(value1);
//    smg2.addValue(value1);
//
//    SMGJoinValues jv = new SMGJoinValues(SMGJoinStatus.EQUAL, smg1, smg2, smgDest, null, null, value1, value1);
//    Assert.assertTrue(jv.isDefined());
//    Assert.assertEquals(SMGJoinStatus.EQUAL, jv.getStatus());
//    Assert.assertSame(smg1, jv.getInputSMG1());
//    Assert.assertSame(smg2, jv.getInputSMG2());
//    Assert.assertSame(smgDest, jv.getDestinationSMG());
//    Assert.assertSame(null, jv.getMapping1());
//    Assert.assertSame(null, jv.getMapping2());
//    Assert.assertEquals(value1, jv.getValue());
//  }

  @Test
	public final void joinValuesAlreadyJoinedTest() {
    smg1.addValue(value1);
    smg2.addValue(value2);
    smgDest.addValue(value3);

    mapping1.map(value1, value3);
    mapping2.map(value2, value3);

    SMGJoinValues jv = new SMGJoinValues(SMGJoinStatus.EQUAL, smg1, smg2, smgDest, mapping1, mapping2, value1, value2);
    Assert.assertTrue(jv.isDefined());
    Assert.assertEquals(SMGJoinStatus.EQUAL, jv.getStatus());
    Assert.assertSame(smg1, jv.getInputSMG1());
    Assert.assertSame(smg2, jv.getInputSMG2());
    Assert.assertSame(smgDest, jv.getDestinationSMG());
    Assert.assertSame(mapping1, jv.getMapping1());
    Assert.assertSame(mapping2, jv.getMapping2());
    Assert.assertEquals(value3, jv.getValue());
  }

  @Test
	public final void joinValuesNonPointers() {
    smg1.addValue(value1);
    smg2.addValue(value2);
    smgDest.addValue(value3);

    mapping1.map(value1, value3);
    SMGJoinValues jv = new SMGJoinValues(SMGJoinStatus.EQUAL, smg1, smg2, smgDest, mapping1, mapping2, value1, value2);
    Assert.assertFalse(jv.isDefined());

    mapping1 = new SMGNodeMapping();
    mapping2.map(value2, value3);
    jv = new SMGJoinValues(SMGJoinStatus.EQUAL, smg1, smg2, smgDest, mapping1, mapping2, value1, value2);
    Assert.assertFalse(jv.isDefined());

    mapping2 = new SMGNodeMapping();

    jv = new SMGJoinValues(SMGJoinStatus.EQUAL, smg1, smg2, smgDest, mapping1, mapping2, value1, value2);
    Assert.assertTrue(jv.isDefined());
    Assert.assertEquals(SMGJoinStatus.EQUAL, jv.getStatus());
    Assert.assertSame(smg1, jv.getInputSMG1());
    Assert.assertSame(smg2, jv.getInputSMG2());
    Assert.assertSame(smgDest, jv.getDestinationSMG());
    Assert.assertSame(mapping1, jv.getMapping1());
    Assert.assertSame(mapping2, jv.getMapping2());
    Assert.assertNotEquals(value1, jv.getValue());
    Assert.assertNotEquals(value2, jv.getValue());
    Assert.assertNotEquals(value3, jv.getValue());
    Assert.assertEquals(jv.getValue(), mapping1.get(value1));
    Assert.assertEquals(jv.getValue(), mapping2.get(value2));
  }

  @Test
	public final void joinValuesSinglePointer() {
    smg1.addValue(value1);
    smg2.addValue(value2);
    smgDest.addValue(value3);

    SMGRegion obj1 = new SMGRegion(SIZE8, "Object");
    SMGEdgePointsTo pt = new SMGEdgePointsTo(value1, obj1, 0);
    smg1.addPointsToEdge(pt);
    SMGJoinValues jv = new SMGJoinValues(SMGJoinStatus.EQUAL, smg1, smg2, smgDest, mapping1, mapping2, value1, value2);
    Assert.assertFalse(jv.isDefined());
  }

}
