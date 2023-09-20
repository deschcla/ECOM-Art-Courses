export class Registration {
  constructor(
    public login: string,
    public firstName: string | null | undefined,
    public lastName: string | null | undefined,
    public email: string,
    // public phone: string,
    public password: string,
    // public birthday: Date,
    public langKey: string
  ) {}
}
