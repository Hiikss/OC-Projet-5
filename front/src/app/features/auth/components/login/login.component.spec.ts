import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {of, throwError} from 'rxjs';
import {AuthService} from '../../services/auth.service';
import {LoginComponent} from './login.component';
import {SessionService} from 'src/app/services/session.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: any;
  let mockRouter: any;
  let mockSessionService: any;

  beforeEach(async () => {
    mockAuthService = {
      login: jest.fn(),
    };

    mockRouter = {
      navigate: jest.fn(),
    };

    mockSessionService = {
      logIn: jest.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [LoginComponent],
      providers: [
        {provide: AuthService, useValue: mockAuthService},
        {provide: Router, useValue: mockRouter},
        {provide: SessionService, useValue: mockSessionService},
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should log in a user and navigate to /sessions', () => {
    const mockResponse = {id: 1, token: 'mockToken', user: {id: 1, email: 'test@test.com'}};
    mockAuthService.login.mockReturnValue(of(mockResponse));

    component.form.setValue({email: 'test@test.com', password: 'password'});
    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith({email: 'test@test.com', password: 'password'});
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockResponse);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true on login failure', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Login failed')));

    component.form.setValue({email: 'test@test.com', password: 'password'});
    component.submit();

    expect(mockAuthService.login).toHaveBeenCalled();
    expect(component.onError).toBe(true);
  });
});
