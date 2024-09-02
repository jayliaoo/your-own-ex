# your-own-ex

Build Your Own Crypto Exchange in Java.

### Motivation

The motivation of me creating this project was that I wanted to find a remote job in web3 such as a crypto exchange, which most of the time requires related work experience, which I don't have. I hope this toy project can help me understand how an exchange works and impress some recruiters and finlay land a remote job. 

### Functional Requirements
- Users must be able to BUY and SELL cryptocurrencies;
- Users must be able to get the latest prices for each crypto; 
- Users are only allowed to place LIMIT orders(i.e. specify at what price they want to BUY or SELL);
- User are only allowed to do Fiat-to-Crypto(USD) trade; 
- Users can only place trades if their account balance allows it.

### Non Functional Requirements
- Highly Consistent — Results of any transactions should be immediately reflected in the order book as well as user accounts;
- Highly Available — System should be always available to place trades; 
- Highly Durable — Trades placed should not get lost; 
- Fairness — Trades placed first should get higher priority to be executed first.

### Roadmap
#### Stage 1
-[x] Backend
  -[x] Basic registration and Login
  -[x] Mock deposit and withdraw funds
  -[x] Basic order matching
  -[x] Basic order execution
#### Stage 2
-[ ] Backend
  -[ ] Integration with Bitcoin testnet.
  -[ ] Integration with Paypal sandbox
  -[ ] Better Order matching
-[ ] Frontend
  -[ ] Basic UI