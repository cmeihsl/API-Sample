# SALES CONDITION CODES

This sample demonstrates the use of sales condition codes in market data. The condition codes are disseminated by markets 
during trading activity and reflect special conditions around instrument or a particular trade. The most common conditions
are:

- [Regular Trade]()
- [Irregular Trade]()
- [Partial Halt - Orderbook can be modified]()
- [Complete Halt - No activity allowed]()

The condition codes and their meaning are dependent on the listing venue. Not all markets have all conditions and even then 
different codes mean different conditions. The API sample shows simple classification of trade conditions on Canadian and American 
markets. For Toronto, the key data point is **PRC_QL2 - the Price Qualifier Code**, which is enumerated to reflect market conditions. For 
US markets, Thomson Reuters no longer enumerates, but passes on the raw codes from market to the client. Client have to consult market
documentation to figure out what it means. Since there can be few thousand conditions for US markets, the API sample, simplifies it by
just checking if any code is present and marking the trade as irregular trade. Same procedure is done for trading halts. 

The sample is written in EMA - Java, but is easily ported to any API and language.


# Build and Run

Define the variable `EMAJ_PATH` and `ETAJ_PATH` which point to the installed EMA and ETA base. Use the provided build and run batch files.
Sample output log is also included.

