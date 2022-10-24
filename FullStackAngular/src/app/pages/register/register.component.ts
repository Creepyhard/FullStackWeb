import { ErrorStateMatcher } from '@angular/material/core';
import { FormControl, Validators, FormGroupDirective, NgForm } from '@angular/forms';
import { OfficeService } from './../../services/office.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { Component, OnInit } from '@angular/core';
import { Office } from 'src/app/model/office';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  user: User = new User();
  users: User[];
  hide = true;
  office: Office[];

  constructor(private userService: UserService, private router: Router,
    private officeService: OfficeService) { }



  email = new FormControl('', [Validators.required, Validators.email]);

  getErrorMessageEmail() {
    if (this.email.hasError('required')) {
      return 'You must enter a value';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  nullFormControl = new FormControl('', [Validators.required, Validators.nullValidator]);

  matcher = new MyErrorStateMatcher();

  ngOnInit(): void {
    this.getUser();
  }

  /*getOffice() {
    this.officeService.getOfficeList().subscribe(data => {
      this.office = data;
    });
  }*/

  getUser() {
    this.userService.getUserList().subscribe(data => {
      this.users = data;
    });
  }

  saveUser() {
    this.userService.addUser(this.user).subscribe(
      data =>{ console.log(data);
      this.goToUserList();
      },
      error => console.log(error));
  }

  goToUserList() {
    this.router.navigate(['/users']);
  }

  onSubmit() {
    console.log(this.user);
    this.saveUser();
  }

}
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}
