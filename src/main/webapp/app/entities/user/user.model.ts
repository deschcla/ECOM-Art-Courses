export interface IUser {
  id: number;
  login?: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string;
  numTel?: string;
}

export class User implements IUser {
  constructor(public id: number, public login: string) {}
}

export function getUserIdentifier(user: IUser): number {
  return user.id;
}
