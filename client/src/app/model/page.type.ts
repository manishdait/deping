export interface Page<T> {
 hasNext: boolean;
 hasPrev: boolean;
 content: T[]; 
}
