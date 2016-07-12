///*|-----------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      --
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  --
// *|           Copyright Thomson Reuters 2015. All rights reserved.            --
///*|-----------------------------------------------------------------------------

import java.util.*;
import com.thomsonreuters.ema.access.*;

class DataCondition 	{
	
	/*
	 *	The list of FID's that our class uses to make decesion. The list varies by listing marketplace
	 */
	private static final int TRDPRC_1 = 6;
	private static final int PRC_QL2 = 131;
	private static final int IRGPRC = 372;
	private static final int IRGCOND = 374;
	private static final int TRD_STATUS = 6614;
	private static final int LSTSALCOND = 4756;
	private static final int IRGSALCOND = 4757;


	public static enum Tradetype	{
		UNKNOWN,
		REGULAR,
		IRREGULAR,
		HALT,
		NOT_HALT
	}

	
	// ------------------------
	public Tradetype getSalesCondition_TOR(FieldList fieldList)	{
	// ------------------------
		// loop over all the fids, and extract the ones of interest
		Iterator<FieldEntry> iter = fieldList.iterator();
		FieldEntry fieldEntry;
		int ql2Code = 0, irgcond = 0;
		
		// get the data points from the field list
		while ( iter.hasNext() )		{
			fieldEntry = iter.next();
			
			if(fieldEntry.code() != Data.DataCode.BLANK)	{
				switch(fieldEntry.fieldId())	{
					case PRC_QL2:
						ql2Code = fieldEntry.enumValue();
						break;
					case IRGCOND:
						irgcond = fieldEntry.enumValue();
						break;
				}
			}
		}
		
		// calculate trade condition based on extracted field data
		switch(ql2Code)	{
			case 0:
			case 242:
			case 12:
			// case 130:	// BYPASS can be regular and irregular trade. Check IRGCOND instead
				return Tradetype.REGULAR;
			
			case 233:
			case 229:
				return Tradetype.HALT;
					
			case 232:
			case 228:
			case 230:
			case 231:
			case 226:
			case 227:
				return Tradetype.NOT_HALT;
				
			case 241:
			case 240:
			case 243:
				return Tradetype.IRREGULAR;
		}
		
		// reached here, check for IRGCOND code
		switch(irgcond)	{
			case 2:
			case 32:
			case 222:
			case 242:
			case 535:
			case 32744:
			case 32746:
			case 32745:
				return Tradetype.IRREGULAR;
		}
			
		return Tradetype.UNKNOWN;
	}
	
	
	// ------------------------
	public Tradetype getSalesCondition_NY(FieldList fieldList)	{
	// ------------------------
		// loop over all the fids, and extract the ones of interest
		Iterator<FieldEntry> iter = fieldList.iterator();
		FieldEntry fieldEntry;
		int tradeStatus = 0;
		String saleCond = "", irgCond = "";

		// get the data points from the field list
		while ( iter.hasNext() )		{
			fieldEntry = iter.next();
			
			if(fieldEntry.code() != Data.DataCode.BLANK)	{
				switch(fieldEntry.fieldId())	{
					case TRD_STATUS:
						tradeStatus = fieldEntry.enumValue();
						break;
					case LSTSALCOND:
						saleCond = fieldEntry.rmtes().toString();
						break;
					case IRGSALCOND:
						irgCond = fieldEntry.rmtes().toString();
						break;
				}
			}
		}
		
		// calculate trade condition based on extracted field data
		switch(tradeStatus)	{
			case 2:
			case 3:
			case 4:
			case 5:
				return Tradetype.HALT;
		}
		
		if(irgCond.length() != 0)
			return Tradetype.IRREGULAR;
			
		return Tradetype.REGULAR;	
	}
	
	
	
	
}


