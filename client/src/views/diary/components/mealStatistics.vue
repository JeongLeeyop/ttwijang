<template>
  <div class="diary2-3__mid">
    <div class="diary2-3__b color2">
      <p class="ti">칼로리</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.calorie / data.standardCalorie * 100)" :color="warningColors"></el-progress>
      <p class="tti">{{ data.calorie | parseKrw }} / {{ data.standardCalorie.toFixed() | parseKrw }}</p>
    </div>

    <div class="diary2-3__b color3">
      <p class="tii">주요 영양소</p>

      <div class="item">
      <p class="tt">탄수화물</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.carbohydrate / data.standardCarbohydrate * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.carbohydrate | parseKrw }}g
        <span v-if="data.carbohydrate - data.standardCarbohydrate > 0" style="display: block; color: #f56c6c">
          ({{ (data.carbohydrate - data.standardCarbohydrate).toFixed() | parseKrw }}g)
        </span>
      </p>
      </div>
      <div class="item">
      <p class="tt">단백질</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.protein / data.standardProtein * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.protein | parseKrw }}g
        <span v-if="data.protein - data.standardProtein > 0" style="display: block; color: #f56c6c">
          ({{ (data.protein - data.standardProtein).toFixed() | parseKrw }}g)
        </span>
      </p>
      </div>
      <div class="item">
      <p class="tt">지방</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.fat / data.standardFat * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.fat | parseKrw }}g
        <span v-if="data.fat - data.standardFat > 0" style="display: block; color: #f56c6c">
          ({{ (data.fat - data.standardFat).toFixed() | parseKrw }}g)
        </span>
      </p>
    </div>
    </div>
    <div class="diary2-3__b color4">
      <p class="tii color2">주의 영양소</p>
      <div class="item">
      <p class="tt">나트륨</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.sodium / data.standardSodium * 100)" :color="warningColors" />
      <p class="tti">
        {{ data.sodium | parseKrw }}mg
        <span v-if="data.sodium - data.standardSodium > 0" style="display: block; color: #f56c6c">
          ({{ (data.sodium - data.standardSodium).toFixed() | parseKrw }}mg)
        </span>
      </p>
      </div>
      <div class="item">
      <p class="tt">당</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.sugar / data.standardSugar * 100)" :color="warningColors" />
      <p class="tti">
        {{ data.sugar | parseKrw }}g
        <span v-if="data.sugar - data.standardSugar > 0" style="display: block; color: #f56c6c">
          ({{ (data.sugar - data.standardSugar).toFixed() | parseKrw }}g)
        </span>
      </p>
      </div>
      <div class="item">
      <p class="tt">포화지방산</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.saturatedFattyAcids / data.standardSaturatedFattyAcids * 100)" :color="warningColors" />
      <div class="tti">
        {{ data.saturatedFattyAcids | parseKrw }}g
        <span v-if="data.saturatedFattyAcids - data.standardSaturatedFattyAcids > 0" style="display: block; color: #f56c6c">
          ({{ (data.saturatedFattyAcids - data.standardSaturatedFattyAcids).toFixed() | parseKrw }}g)
        </span>
      </div>
      </div>
    </div>

    <div class="diary2-3__b color5">
      <p class="tii color3">기타 영양소</p>
      <div class="item">
      <p class="tt">트랜스지방</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.transFat / data.standardTransFat * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.transFat | parseKrw }}g
        <span v-if="data.transFat - data.standardTransFat > 0" style="display: block; color: #f56c6c">
          ({{ (data.transFat - data.standardTransFat).toFixed() | parseKrw }}g)
        </span>
      </p>
      </div>
      <div class="item">
      <p class="tt">콜레스테롤</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.cholesterol / data.standardCholesterol * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.cholesterol | parseKrw }}mg
        <span v-if="data.cholesterol - data.standardCholesterol > 0" style="display: block; color: #f56c6c">
          ({{ (data.cholesterol - data.standardCholesterol).toFixed() | parseKrw }}g)
        </span>
      </p>
    </div>
      <div class="item">
      <p class="tt">칼슘</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.calcium / data.standardCalcium * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.calcium | parseKrw }}g
        <span v-if="data.calcium - data.standardCalcium > 0" style="display: block; color: #f56c6c">
          ({{ (data.calcium - data.standardCalcium).toFixed() | parseKrw }}g)
        </span>
      </p>
    </div>
      <div class="item">
      <p class="tt">철</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.iron / data.standardIron * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.iron | parseKrw }}g
        <span v-if="data.iron - data.standardIron > 0" style="display: block; color: #f56c6c">
          ({{ (data.iron - data.standardIron).toFixed() | parseKrw }}g)
        </span>
      </p>
    </div>
      <div class="item">
      <p class="tt">칼륨</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.potassium / data.standardPotassium * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.potassium | parseKrw }}g
        <span v-if="data.potassium - data.standardPotassium > 0" style="display: block; color: #f56c6c">
          ({{ (data.potassium - data.standardPotassium).toFixed() | parseKrw }}g)
        </span>
      </p>
    </div>
      <div class="item">
      <p class="tt">식이섬유</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.dietaryFiber / data.standardDietaryFiber * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.dietaryFiber | parseKrw }}g
        <span v-if="data.dietaryFiber - data.standardDietaryFiber > 0" style="display: block; color: #f56c6c">
          ({{ (data.dietaryFiber - data.standardDietaryFiber).toFixed() | parseKrw }}g)
        </span>
      </p>
    </div>
      <div class="item">
      <p class="tt">비타민C</p>
      <el-progress :text-inside="true" :stroke-width="7" :percentage="(data.vitaminC / data.standardVitaminC * 100)" :color="warningColors"></el-progress>
      <p class="tti">
        {{ data.vitaminC | parseKrw }}g
        <span v-if="data.vitaminC - data.standardVitaminC > 0" style="display: block; color: #f56c6c">
          ({{ (data.vitaminC - data.standardVitaminC).toFixed() | parseKrw }}g)
        </span>
      </p>
    </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  Vue,
  Component,
  Prop,
} from 'vue-property-decorator';

@Component({
})

export default class extends Vue {
  @Prop({ required: true }) private data!: any;

  private warningColors = [
    { color: '#58AAD6', percentage: 80 },
    { color: '#e6a23c', percentage: 90 },
    { color: '#f56c6c', percentage: 100 },
  ]
}
</script>
