import {Teacher} from "../../src/app/interfaces/teacher.interface";
import {Session} from "../../src/app/features/sessions/interfaces/session.interface";

describe('Session spec', () => {
  const session: Session = {
    id: 1,
    name: 'session test',
    description: 'description',
    date: new Date('2024-11-22T00:00:00Z'),
    teacher_id: 1,
    users: [2],
    createdAt: new Date('2024-11-22T00:00:00Z'),
    updatedAt: new Date('2024-11-22T00:00:00Z'),
  };

  const teachers: Teacher[] = [
    {
      id: 1,
      firstName: 'Margot',
      lastName: 'DELAHAYE',
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      firstName: 'Hélène',
      lastName: 'THIERCELIN',
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  ];

  const teacherName = `${teachers[0].firstName} ${teachers[0].lastName}`;

  beforeEach(() => {
    cy.intercept('GET', '/api/teacher', {
      statusCode: 200,
      body: teachers,
    });

    cy.intercept('POST', '/api/session', {
      status: 200,
    });

    cy.intercept('GET', '/api/session', [
      {
        ...session,
        date: new Date(),
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ]);
  });

  it('Login successful', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.url().should('include', '/sessions');
  });

  it('Session create failed', () => {
    cy.contains('Rentals available').should('be.visible');
    cy.get('.mat-raised-button').eq(0).click();
    cy.url().should('include', '/create');

    cy.get('input[formControlName=name]').type(session.name);
    cy.get('input[formControlName=date]').type(session.date.toISOString().split('T')[0]);
    cy.get('textarea[formControlName=description]').type(session.description);

    cy.get('button[type=submit]').should('be.disabled');
  });

  it('Session created', () => {
    cy.url().should('include', '/create');

    cy.get('input[formControlName=name]').type(session.name);
    cy.get('input[formControlName=date]').type(session.date.toISOString().split('T')[0]);
    cy.get('textarea[formControlName=description]').type(session.description);

    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('.cdk-overlay-container .mat-select-panel .mat-option-text').should('contain', teacherName);
    cy.get(`.cdk-overlay-container .mat-select-panel .mat-option-text:contains("${teacherName}")`).first().click();
    cy.get('[formcontrolname=teacher_id]').contains(teacherName);

    cy.get('button[type=submit]').click();
    cy.url().should('include', '/session');
  });

  it('Display session details', () => {
    cy.intercept('GET', '/api/session/1', session);
    cy.get('button').contains('Detail').click()

    cy.get('h1').should('contain', 'Test');
    cy.get('h1').should('contain', 'Test');
    cy.get('.ml1').should('contain', '1 attendees');
    cy.get('.ml1').should('contain', 'November 22, 2024');
    cy.get('.description').should('contain', 'Description: description');
    cy.get('.created').should('contain', 'Create at:  November 22, 2024');
    cy.get('.updated').should('contain', 'Last update:  November 22, 2024');
  })

  it('Participate a session', () => {
    cy.intercept('POST', '/api/session/1/participate/1', {
      statusCode:200
    })
    cy.login(false)
    cy.intercept('GET', '/api/session/1', session).as('sessionDetail');
    cy.get('button').contains('Detail').click()
    cy.get('button').contains('Participate').click()
  })

  it('Unparticipate a session', () => {
    const session: Session = {
      id: 1,
      name: 'session test',
      description: 'description',
      date: new Date('2024-11-22T00:00:00Z'),
      teacher_id: 1,
      users: [1],
      createdAt: new Date('2024-11-22T00:00:00Z'),
      updatedAt: new Date('2024-11-22T00:00:00Z'),
    };

    cy.intercept('GET', '/api/session', [
      {
        ...session,
        date: new Date(),
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ]);

    cy.login(false)

    cy.intercept('POST', '/api/session/1/unparticipate/1', {
      statusCode:200
    })
    cy.intercept('GET', '/api/session/1', session);
    cy.get('button').contains('Detail').click()
    cy.get('button').contains('Do not participate').click()
  })

  it('Delete a session', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept('DELETE', '/api/session/1', {
      statusCode: 200,
      body: {}
    });

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.intercept('GET', '/api/session/1', session);
    cy.get('button').contains('Detail').click()
    cy.get('button').contains('Delete').click()

    cy.url().should('include', '/');
    cy.get('.mat-snack-bar-container').should('contain', 'Session deleted !');
  });
});
