import { Module, VuexModule, Mutation } from 'vuex-module-decorators';

export interface TeamCreationFormData {
  name?: string
  code?: string
  sponsorBank?: string
  sponsorAccount?: string
  logo?: string
  logoFileUid?: string
}

export interface TeamCreationInfoData {
  name?: string
  logo?: string
  activeDays?: string[]
  activeTimes?: string[]
  gender?: string
  ageRanges?: string[]
}

export interface TeamCreationLocationData {
  stadiumName?: string
  city?: string
  district?: string
}

@Module({ namespaced: true, name: 'teamCreation' })
export default class TeamCreationModule extends VuexModule {
  formData: TeamCreationFormData = {}

  infoData: TeamCreationInfoData = {}

  locationData: TeamCreationLocationData = {}

  @Mutation
  SET_FORM_DATA(data: TeamCreationFormData): void {
    this.formData = data;
  }

  @Mutation
  SET_INFO_DATA(data: TeamCreationInfoData): void {
    this.infoData = data;
  }

  @Mutation
  SET_LOCATION_DATA(data: TeamCreationLocationData): void {
    this.locationData = data;
  }

  @Mutation
  CLEAR(): void {
    this.formData = {};
    this.infoData = {};
    this.locationData = {};
  }
}
