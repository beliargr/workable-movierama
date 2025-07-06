import {Component, OnInit, ViewChild} from '@angular/core';
import {ConfirmationService, MessageService} from 'primeng/api';
import {Table, TableModule} from 'primeng/table';
import {CommonModule, DatePipe} from '@angular/common';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ButtonModule} from 'primeng/button';
import {RippleModule} from 'primeng/ripple';
import {ToastModule} from 'primeng/toast';
import {ToolbarModule} from 'primeng/toolbar';
import {RatingModule} from 'primeng/rating';
import {InputTextModule} from 'primeng/inputtext';
import {TextareaModule} from 'primeng/textarea';
import {SelectModule} from 'primeng/select';
import {RadioButtonModule} from 'primeng/radiobutton';
import {InputNumberModule} from 'primeng/inputnumber';
import {DialogModule} from 'primeng/dialog';
import {TagModule} from 'primeng/tag';
import {InputIconModule} from 'primeng/inputicon';
import {IconFieldModule} from 'primeng/iconfield';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {IMovie, IMoviesWithRatings, MovieService} from "../service/movie.service";
import {Observable, of, tap} from "rxjs";
import {ILookup, LookupService} from "../service/lookup.service";
import {NGXLogger} from "ngx-logger";
import {DatePickerModule} from "primeng/datepicker";
import {IRating, IRatingType, RatingService} from "../service/rating.service";
import {AuthService} from "../../auth/service/auth.service";

@Component({
    selector: 'app-crud',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        FormsModule,
        ButtonModule,
        RippleModule,
        ToastModule,
        ToolbarModule,
        RatingModule,
        InputTextModule,
        TextareaModule,
        SelectModule,
        RadioButtonModule,
        InputNumberModule,
        DialogModule,
        TagModule,
        InputIconModule,
        IconFieldModule,
        ConfirmDialogModule,
        TranslateModule,
        ReactiveFormsModule,
        DatePickerModule
    ],
    templateUrl: `movies.component.html`,
    providers: [MessageService, ConfirmationService, MovieService, LookupService, NGXLogger, DatePipe, RatingService]
})
export class MoviesComponent implements OnInit {
    productDialog: boolean = false;
    movies: Observable<IMoviesWithRatings[]> = of([]);
    selectedProducts!: IMoviesWithRatings[] | null;
    submitted: boolean = false;
    loading: boolean = false;
    @ViewChild('dt') dt!: Table;
    form: FormGroup;
    $lookups: Observable<{ [key: string]: ILookup[] }>;

    username: string;

    constructor(
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private translateService: TranslateService,
        private movieService: MovieService,
        private ratingService: RatingService,
        private fb: FormBuilder,
        protected authService: AuthService,
        private lookupService: LookupService,
        private logger: NGXLogger) {
        this.form = fb.group({
            id: new FormControl(null),
            title: new FormControl(null, [Validators.required]),
            description: new FormControl(null, [Validators.required]),
            genre: new FormControl(null, [Validators.required]),
            createdBy: new FormControl({value: null, disabled: true}),
            createdDate: new FormControl({value: null, disabled: true}),
            lastModifiedDate: new FormControl({value: null, disabled: true}),
        });
        this.$lookups = this.lookupService.getLookups();
        this.username = this.authService.profile.username;
    }

    ngOnInit() {
        this.getData();
    }

    getData() {
        this.loading = true;
        this.movies = this.movieService.getMoviesWithRatings().pipe(
            tap(() => this.loading = false)
        );
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.form.reset();
        this.submitted = false;
        this.productDialog = true;
    }

    edit(movie: IMovie) {
        this.form.reset();
        this.form.patchValue({
            id: movie.id,
            title: movie.title,
            description: movie.description,
            genre: movie.genre,
            createdDate: movie.createdDate ? new Date(movie.createdDate) : movie.createdDate,
            createdBy: movie.createdBy,
            lastModifiedDate: movie.lastModifiedDate ? new Date(movie.lastModifiedDate) : movie.lastModifiedDate
        });
        this.productDialog = true;
    }

    deleteSelectedProducts() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete the selected movies?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.movieService.bulkDelete(this.selectedProducts.map(_ => _.id)).subscribe(
                    {
                        next: (value: IMovie) => {
                            this.logger.info('Movie deleted successfully');
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Movies deleted Successfully',
                                life: 3000
                            });
                        },
                        complete: () => {
                            this.getData();
                            this.selectedProducts = null;
                        }
                    }
                )
            }
        });
    }

    hideDialog() {
        this.productDialog = false;
        this.submitted = false;
    }

    delete(record: IMovie) {
        this.logger.debug(`Deleting record with id: ${record.id}`)
        this.movieService
            .delete(record.id).subscribe({
                next: (value: IMovie) => {
                    this.logger.info('Movie deleted successfully');
                    this.messageService.add({
                        severity: 'success',
                        summary: this.translateService.instant('crud.successfulDeletion'),
                        life: 3000
                    });
                },
                complete: () => {
                    this.getData();
                }
            }
        );
    }

    save() {
        const formValue: IMovie = this.form.getRawValue();
        this.logger.debug(`Form submitted with value: ${formValue}`)
        this.movieService
            .save(formValue).subscribe({
                next: (value: IMovie) => {
                    this.logger.info('Movies Updated Successfully');
                    this.messageService.add({
                        severity: 'success',
                        summary: this.translateService.instant('crud.successfulUpdate'),
                        life: 3000
                    });
                },
                complete: () => {
                    this.productDialog = false;
                    this.getData();
                }
            }
        );
    }

    rate(record: IMoviesWithRatings, type: IRatingType) {
        let $obs: Observable<any>;
        if (!type) {
            $obs = this.ratingService.delete(record.ratingId);
        } else {
            const payload: IRating = {movieId: record.id, type: type};
            $obs = this.ratingService.rate(payload)
        }
        $obs.subscribe({
            next: (value: IMovie) => {
                this.logger.info('Movies Updated Successfully');
                this.messageService.add({
                    severity: 'success',
                    summary: this.translateService.instant('crud.successfulUpdate'),
                    life: 3000
                });
            },
            complete: () => {
                this.getData();
            }
        });
    }

    protected readonly IRatingType = IRatingType;

}
