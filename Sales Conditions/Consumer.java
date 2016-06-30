///*|-----------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      --
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  --
// *|           Copyright Thomson Reuters 2015. All rights reserved.            --
///*|-----------------------------------------------------------------------------

import java.util.Iterator;
import com.thomsonreuters.ema.access.*;
import com.thomsonreuters.ema.access.DataType.*;
import com.thomsonreuters.ema.rdm.*;


class AppClient implements OmmConsumerClient	{
	
	public static enum Market	{
		TORONTO,
		NY
	}
	
	DataCondition dC = new DataCondition();

	public void onRefreshMsg( RefreshMsg refreshMsg, OmmConsumerEvent event )	{
		System.out.println( "Item Name: " + ( refreshMsg.hasName() ? refreshMsg.name() : "<not set>" ) );
		System.out.println( "Service Name: " + ( refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>" ) );
		System.out.println( "Item State: " + refreshMsg.state() );
		
		if ( DataType.DataTypes.FIELD_LIST == refreshMsg.payload().dataType() )
			decode( refreshMsg.payload().fieldList(), (Market) event.closure() );
		
		System.out.println();
	}
	
	public void onUpdateMsg( UpdateMsg updateMsg, OmmConsumerEvent event ) 	{
		System.out.println( "Item Name: " + ( updateMsg.hasName() ? updateMsg.name() : "<not set>" ) );
		System.out.println( "Service Name: " + ( updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>" ) );
		
		if ( DataType.DataTypes.FIELD_LIST == updateMsg.payload().dataType() )
			decode( updateMsg.payload().fieldList(), (Market) event.closure() );
		
		System.out.println();
	}

	public void onStatusMsg( StatusMsg statusMsg, OmmConsumerEvent event ) 	{
		System.out.println( "Item Name: " + ( statusMsg.hasName() ? statusMsg.name() : "<not set>" ) );
		System.out.println( "Service Name: " + ( statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>" ) );

		if ( statusMsg.hasState() )
			System.out.println( "Item State: " +statusMsg.state() );
		
		System.out.println();
	}
	
	public void onGenericMsg( GenericMsg genericMsg, OmmConsumerEvent consumerEvent )	{}
	public void onAckMsg( AckMsg ackMsg, OmmConsumerEvent consumerEvent )	{}
	public void onAllMsg( Msg msg, OmmConsumerEvent consumerEvent )		{}

	/*
	 * Typical decode method  for Level 1 OMM Field list data
	 */
	void decode(FieldList fieldList, Market mkt)	{
		
		Iterator<FieldEntry> iter = fieldList.iterator();
		FieldEntry fieldEntry;
		while ( iter.hasNext() )		{
			fieldEntry = iter.next();
			/*
			 *	Print out raw fields on console
			 */
			System.out.println(fieldEntry.name() + "(" + fieldEntry.fieldId() + "): " + fieldEntry.load() );
		}
		

		/*
		 *	Extract the condition code and its meaning
		 */
		DataCondition.Tradetype condCode = DataCondition.Tradetype.UNKNOWN;
		if(mkt == Market.TORONTO)
			condCode = dC.getSalesCondition_TOR(fieldList);
		else if (mkt == Market.NY)
			condCode = dC.getSalesCondition_NY(fieldList);
		
		
		/*
		 *	Print it on console
		 */
		System.out.println("------------------------");
		System.out.printf("Trade type: %s\n", condCode);
		System.out.println("------------------------");
	}
	
}


public class Consumer 	{
	
	public static void main( String[] args )	{
		try		{
			AppClient appClient = new AppClient();
			OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();
			
			// define the market data endpoint here
			OmmConsumer consumer  = EmaFactory.createOmmConsumer( config.host( "TOR_ADS:14002"  ).username( "user" ) );

			// We subscribe to two instruments listed on two different markets
			consumer.registerClient( EmaFactory.createReqMsg().serviceName( "ELEKTRON_AD" ).name( "TD.TO" ), appClient, AppClient.Market.TORONTO );
			consumer.registerClient( EmaFactory.createReqMsg().serviceName( "ELEKTRON_AD" ).name( "IBM.N" ), appClient, AppClient.Market.NY );

			// Wait for a while
			Thread.sleep( 1200000 );
		}
		catch ( Exception e )		{
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	
}


