export interface IUser {
  id: number;
  firstName?: string;
  lastName?: string;
  login?: string;
}

export class User implements IUser {
  constructor(public id: number, public firstName: string, public lastName: string, public login: string) {}
}

export function getUserIdentifier(user: IUser): number {
  return user.id;
}
