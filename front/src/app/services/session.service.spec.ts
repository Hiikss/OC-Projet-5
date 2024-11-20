import {TestBed} from '@angular/core/testing';
import {SessionService} from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService],
    });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('isLogged initialization', () => {
    it('should initialize isLogged to false', () => {
      expect(service.isLogged).toBe(false);
    });

    it('should initialize sessionInformation to undefined', () => {
      expect(service.sessionInformation).toBeUndefined();
    });
  });

  describe('$isLogged', () => {
    it('should return an observable of isLogged', (done) => {
      service.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(false);
        done();
      });
    });
  });

  describe('logIn', () => {
    it('should set sessionInformation and isLogged to true', () => {
      const mockSession: SessionInformation = {
        token: '12345',
        type: 'test',
        id: 1,
        username: 'test',
        firstName: 'test',
        lastName: 'test',
        admin: false
      };
      service.logIn(mockSession);

      expect(service.sessionInformation).toEqual(mockSession);
      expect(service.isLogged).toBe(true);
    });

    it('should emit true via isLoggedSubject', (done) => {
      const mockSession: SessionInformation = {
        token: '12345',
        type: 'test',
        id: 1,
        username: 'test',
        firstName: 'test',
        lastName: 'test',
        admin: false
      };

      service.$isLogged().subscribe((isLogged) => {
        if (isLogged) {
          expect(isLogged).toBe(true);
          done();
        }
      });

      service.logIn(mockSession);
    });
  });

  describe('logOut', () => {
    it('should clear sessionInformation and set isLogged to false', () => {
      const mockSession: SessionInformation = {
        token: '12345',
        type: 'test',
        id: 1,
        username: 'test',
        firstName: 'test',
        lastName: 'test',
        admin: false
      };
      service.logIn(mockSession);

      service.logOut();

      expect(service.sessionInformation).toBeUndefined();
      expect(service.isLogged).toBe(false);
    });

    it('should emit false via isLoggedSubject', (done) => {
      const mockSession: SessionInformation = {
        token: '12345',
        type: 'test',
        id: 1,
        username: 'test',
        firstName: 'test',
        lastName: 'test',
        admin: false
      };
      service.logIn(mockSession);

      service.$isLogged().subscribe((isLogged) => {
        if (!isLogged) {
          expect(isLogged).toBe(false);
          done();
        }
      });

      service.logOut();
    });
  });

  describe('next', () => {
    it('should emit the current isLogged value via isLoggedSubject', (done) => {
      service['next']();

      service.$isLogged().subscribe((isLogged) => {
        expect(isLogged).toBe(service.isLogged);
        done();
      });
    });
  });
});
