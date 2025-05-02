export interface WalletDto {
  uname: string; 
  accountId: string;
  pubKey: string;
  balance: number;
}

export interface TransferHbarRequest {
  pubKey: string;
  amount: number;
}
