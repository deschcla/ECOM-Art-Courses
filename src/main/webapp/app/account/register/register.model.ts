export class Registration {
  constructor(
    public login: string,
    public email: string,
    public phone: string,
    public password: string,
    public birthday: Date,
    public langKey: string,
  ) {}
}
